"use strict";

function createExpression(constructor, evaluate, toString, diff, prefix) {
    constructor.prototype.evaluate = evaluate;
    constructor.prototype.toString = toString;
    constructor.prototype.diff = diff;
    constructor.prototype.prefix = prefix;
}

function Const(value) {
    this._value = value;
}
createExpression(
    Const,
    function() { return +this._value},
    function() { return "" + this._value},
    function() { return Const.ZERO},
    function() { return this.toString()}
);

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);

let VAR_INDEX = {
    "x": 0,
    "y": 1,
    "z": 2
};
function Variable(name) {
    this._name = name;
    this._ind = VAR_INDEX[name];
}
createExpression(
    Variable,
    function(...vars) { return vars[this._ind]},
    function() { return this._name},
    function(name) { return this._name === name ? Const.ONE : Const.ZERO},
    function() { return this.toString()});

function Operation(...args) {
    this._args = args;
}
createExpression(
    Operation,
    function(...vars) { return this._op(...this._args.map(val => val.evaluate(...vars)))},
    function() { return this._args.join(" ") + " " + this._operator},
    function (name) { return this._diffRule(name, ...this._args)},
    function() { return "(" + this._operator + " " + this._args.map(el => el.prefix()).join(" ") + ")"});

function createOperation(_op, _operator, _diffRule) {
    let constructor = function(...args) {
        Operation.call(this, ...args);
    };
    constructor.prototype = Object.create(Operation.prototype);
    constructor.prototype._op = _op;
    constructor.prototype._operator = _operator;
    constructor.prototype._diffRule = _diffRule;
    return constructor;
}

let Negate = createOperation(
    x => -x,
    "negate",
    function (name, x) {return new Negate(x.diff(name))
    });

const Add = createOperation(
    (a, b) => a + b,
    "+",
    function(name, a, b) { return new Add(a.diff(name), b.diff(name))});

const Subtract = createOperation(
    (a, b) => a - b,
    "-",
    function(name, a, b) { return new Subtract(a.diff(name), b.diff(name))});

const Multiply = createOperation(
    (a, b) => a * b,
    "*",
    function(name, a, b) {
        return new Add(
            new Multiply(a.diff(name), b),
            new Multiply(a, b.diff(name)))});

const Divide = createOperation(
    (a, b) => a / b,
    "/",
    function(name, a, b) {
        return new Divide(
            new Subtract(
                new Multiply(a.diff(name), b),
                new Multiply(a, b.diff(name))),
            new Multiply(b, b))});

const Gauss = createOperation(
    (a, b, c, x) => a * Math.exp(-(x - b) * (x - b) / (2 * c * c)),
    "gauss",
    function(name, a, b, c, x) {
        return new Add(
            new Multiply(
                a.diff(name),
                new Gauss(Const.ONE, b, c, x)),
            new Multiply(
                a,
                new Multiply(
                    new Gauss(Const.ONE, b, c, x),
                    Negate.prototype._diffRule(
                        name,
                        new Divide(
                            new Multiply(new Subtract(x, b), new Subtract(x, b)),
                            new Multiply(Const.TWO, new Multiply(c, c)))))))});

const TOKEN_TO_OPERATION = {
    "negate": Negate,
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "gauss": Gauss
};

function parse(expression) {
    let stack = [];
    for (const token of expression.trim().split(/\s+/)) {
        if (token in TOKEN_TO_OPERATION) {
            const operation = TOKEN_TO_OPERATION[token];
            stack.push(new operation(...stack.splice(-operation.prototype._op.length)));
        } else if (token in VAR_INDEX) {
            stack.push(new Variable(token));
        } else {
            stack.push(new Const(token));
        }
    }
    return stack.pop();
}

function ExpressionError(message) {
    this.message = message;
}
ExpressionError.prototype = Object.create(Error.prototype);

function BracketError(pos, expected, isOpen) {
    this.message = (expected ? "Expected " : "Unexpected ") + (isOpen ? "open " : "close ") + "bracket at pos: " + pos;
}
BracketError.prototype = Object.create(ExpressionError.prototype);

function TokenError(pos, expected) {
    this.message = (expected ? "Expected " : "Unexpected ") + "token at pos: " + pos;
}
TokenError.prototype = Object.create(ExpressionError.prototype);

function UnexpectedFunctionError(pos) {
    this.message = "Unexpected function at pos: " + pos;
}
UnexpectedFunctionError.prototype = Object.create(ExpressionError.prototype);

const parsePrefix = getParser(parsePrefixBracket);

function getParser(parseBracket) {
    return function (expression) {
        expression = expression.trim();
        const source = new Source(expression);
        function getExpression() {
            let token = source.nextToken();
            if (!isNaN(+token)) {
                return new Const(+token);
            } else if (token in VAR_INDEX) {
                return new Variable(token);
            } else if (token === ")") {
                throw new BracketError(source.getPos(), false, false);
            } else if (token in TOKEN_TO_OPERATION) {
                throw new UnexpectedFunctionError(source.getPos());
            } else if (token === "(") {
                return parseBracket(source, getExpression);
            } else {
                throw new TokenError(source.getPos(), false);
            }
        }
        const res = getExpression();
        if (source.getPos() !== expression.length) {
            throw new TokenError(source.getPos, false);
        }
        return res;
    }
}

function parsePrefixBracket(source, getExpression) {
    let token = source.nextToken();
    if (!token in TOKEN_TO_OPERATION) {
        throw new TokenError(source.getPos(), false);
    }
    let operation = TOKEN_TO_OPERATION[token];
    let args = [];
    for (let i = 0; i < operation.prototype._op.length; i++) {
        args.push(getExpression());
    }
    token = source.nextToken();
    if (token !== ")") {
        throw new BracketError(source.getPos(), true, false);
    }
    return new operation(...args);
}

function Source(expression) {
    let pos = 0;
    this.nextToken = function() {
        while (expression[pos] === " ") {
            pos++;
        }
        if (pos === expression.length) {
            throw new TokenError(pos, true);
        }
        if (expression[pos] === "(" || expression[pos] === ")") {
            return expression[pos++];
        }
        if (isDigit(expression[pos]) || (expression[pos] === "-" && isDigit(expression[pos + 1]))) {
            let token = [];
            if (expression[pos] === "-") {
                token.push(expression[pos++]);
            }
            for (; isDigit(expression[pos]); pos++) {
                token.push(expression[pos]);
            }
            return token.join("");
        } else {
            for (let token in VAR_INDEX) {
                if (checkToken(token)) {
                    return token;
                }
            }
            for (let token in TOKEN_TO_OPERATION) {
                if (checkToken(token)) {
                    return token;
                }
            }
            throw new TokenError(pos, true);
        }
    };
    function checkToken(token) {
        for (let i = 0; i < token.length; i++) {
            if (expression[pos + i] !== token[i]) {
                return false;
            }
        }
        pos = pos + token.length;
        return true;
    }
    this.getPos = () => pos;
}

function isDigit(c) {
    return c >= "0" && c <= "9";
}