"use strict";

function createExpression(constructor, evaluate, toString, diff, prefix = toString, postfix = toString) {
    constructor.prototype.evaluate = evaluate;
    constructor.prototype.toString = toString;
    constructor.prototype.diff = diff;
    constructor.prototype.prefix = prefix;
    constructor.prototype.postfix = postfix;
}

function Const(value) {
    this._value = value;
}
createExpression(
    Const,
    function() { return +this._value},
    function() { return "" + this._value},
    function() { return Const.ZERO});

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
    function(name) { return this._name === name ? Const.ONE : Const.ZERO});

function Operation(...args) {
    this._args = args;
}
createExpression(
    Operation,
    function(...vars) { return this._op(...this._args.map(val => val.evaluate(...vars)))},
    function() { return this._args.join(" ") + " " + this._operator},
    function (name) { return this._diffRule(name, ...this._args)},
    function() { return "(" + this._operator + " " + this._args.map(el => el.prefix()).join(" ") + ")"},
    function() { return "(" + this._args.map(el => el.postfix()).join(" ") + " " + this._operator + ")"});

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
    (name, x) => new Negate(x.diff(name)));

const Add = createOperation(
    (a, b) => a + b,
    "+",
    (name, a, b) => new Add(a.diff(name), b.diff(name)));

const Subtract = createOperation(
    (a, b) => a - b,
    "-",
    (name, a, b) => new Subtract(a.diff(name), b.diff(name)));

const Multiply = createOperation(
    (a, b) => a * b,
    "*",
    (name, a, b) => new Add(
            new Multiply(a.diff(name), b),
            new Multiply(a, b.diff(name))));

const Divide = createOperation(
    (a, b) => a / b,
    "/",
    (name, a, b) => new Divide(
            new Subtract(
                new Multiply(a.diff(name), b),
                new Multiply(a, b.diff(name))),
            new Multiply(b, b)));

const Mean = createOperation(
    (...args) => (args.reduce((s, x) => s + x, 0)) / args.length,
    "mean",
    (name, ...args) => Divide.prototype._diffRule(
            name,
            args.reduce((s, x) => new Add(s, x), Const.ZERO),
            new Const(args.length)));

const Var = createOperation(
    (...args) => {
        const mean = Mean.prototype._op(...args);
        return Mean.prototype._op(...args.map(el => (el - mean) * (el - mean)));
    },
    "var",
    (name, ...args) => {
        const mean = new Mean(...args);
        return Mean.prototype._diffRule(
            name,
            ...args.map(el => {const sub = new Subtract(el, mean); return new Multiply(sub, sub)}));});

const TOKEN_TO_OPERATION = {
    "negate": Negate,
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "mean": Mean,
    "var": Var
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

function BracketError(pos, isExpected, isOpen) {
    this.message = getExpected(isExpected) + getOpen(isOpen) + "bracket at pos: " + pos;
}
BracketError.prototype = Object.create(ExpressionError.prototype);

function TokenError(pos, isExpected, token) {
    this.message = getExpected(isExpected) + "token " + (!isExpected ? "(" + token + ") " : "") + "at pos: " + pos;
}
TokenError.prototype = Object.create(ExpressionError.prototype);

function OperationError(pos, isExpected) {
    this.message = getExpected(isExpected) + "operation at pos: " + pos;
}
OperationError.prototype = Object.create(ExpressionError.prototype);

function UnsupportedArgumentsError(pos, op) {
    this.message = "Operator (" + op + ") at pos " + pos + " can't apply such arguments";
}
UnsupportedArgumentsError.prototype = Object.create(ExpressionError.prototype);

function InvalidTokenError(pos, token) {
    this.message = "Invalid token (" + token + ") at pos: " + pos;
}
InvalidTokenError.prototype = Object.create(ExpressionError.prototype);

const getMessageGetter = (iff, els) => (fl) => (fl ? iff : els)
const getExpected = getMessageGetter("Expected ", "Unexpected ");
const getOpen = getMessageGetter("open ", "close ");

const getParser = (parseBracket) => {
    return (expression) => {
        expression = expression.trim();
        const source = new Source(expression);
        function getExpression() {
            const token = source.nextToken();
            if (!isNaN(+token)) {
                return new Const(+token);
            } else if (token in VAR_INDEX) {
                return new Variable(token);
            } else if (token === ")") {
                throw new BracketError(source.getPos(), false, false);
            } else if (token in TOKEN_TO_OPERATION) {
                throw new OperationError(source.getPos(), false);
            } else if (token === "(") {
                return parseBracket(source, getExpression);
            } else {
                throw new TokenError(source.getPos(), false, token);
            }
        }
        const res = getExpression();
        if (source.checkNextToken() !== undefined) {
            throw new TokenError(source.getPos(), false, source.checkNextToken());
        }
        return res;
    }
};

const parsePrefixBracket = (source, getExpression) => {
    const operation = getOperation(source);
    const args = parseArgs(source, getExpression);
    checkBracket(source, operation, args);
    return new operation(...args);
};

const parsePostfixBracket = (source, getExpression) => {
    const args = parseArgs(source, getExpression);
    const operation = getOperation(source);
    checkBracket(source, operation, args);
    return new operation(...args);
};

const parsePrefix = getParser(parsePrefixBracket);

const parsePostfix = getParser(parsePostfixBracket);

function Source(expression) {
    let pos = 0;
    const _nextToken = () => {
        while (expression[pos] === " ") {
            pos++;
        }
        if (pos === expression.length) {
            return undefined;
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
            let token = [];
            for (; expression[pos] !== " " && expression[pos] !== undefined; pos++) token.push(expression[pos]);
            throw new InvalidTokenError(pos, token.join(""));
        }
    };
    const checkToken = (token) => {
        for (let i = 0; i < token.length; i++) {
            if (expression[pos + i] !== token[i]) {
                return false;
            }
        }
        pos = pos + token.length;
        return true;
    };
    let curToken = _nextToken();
    this.checkNextToken = () => curToken;
    this.nextToken = () => {
        const token = this.checkNextToken();
        if (token === undefined) {
            throw new TokenError(pos, true);
        }
        curToken = _nextToken();
        return token;
    };
    this.getPos = () => pos;
}

const isDigit = (c) => {
    return c >= "0" && c <= "9";
};

const parseArgs = (source, getExpression) => {
    let args = [];
    while (!(source.checkNextToken() in TOKEN_TO_OPERATION || source.checkNextToken() === ")" || source.checkNextToken() === undefined)) {
        args.push(getExpression());
    }
    return args;
};

const checkBracket = (source, operation, args) => {
    if (operation.prototype._op.length !== 0 && args.length !== operation.prototype._op.length) {
        throw new UnsupportedArgumentsError(source.getPos(), operation.prototype._operator);
    }
    if (source.checkNextToken() !== ")") {
        throw new BracketError(source.getPos(), true, false);
    } else {source.nextToken();}
};

const getOperation = (source) => {
    if (!(source.checkNextToken() in TOKEN_TO_OPERATION)) {
        throw new OperationError(source.getPos(), true);
    }
    return TOKEN_TO_OPERATION[source.nextToken()];
};