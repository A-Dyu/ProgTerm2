"use strict";

function createExpression(constructor, evaluate, toString, diff) {
    constructor.prototype.evaluate = evaluate;
    constructor.prototype.toString = toString;
    constructor.prototype.diff = diff;
}

function Const(value) {
    this._value = value;
}
createExpression(
    Const,
    function() { return +this._value},
    function() { return "" + this._value},
    function() { return Const.ZERO}
);

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);

const varIndex = {
    "x": 0,
    "y": 1,
    "z": 2
};
function Variable(name) {
    this._name = name;
    this._ind = varIndex[name];
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
    function (name) { return this._diffRule(name, ...this._args)});

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

let Negate = createOperation(x => -x, "negate",
    function (name, x) {
        return new Negate(x.diff(name))
    });

const Add = createOperation((a, b) => a + b, "+",
    function(name, a, b) { return new Add(a.diff(name), b.diff(name))});

const Subtract = createOperation((a, b) => a - b, "-",
    function(name, a, b) { return new Subtract(a.diff(name), b.diff(name))});

const Multiply = createOperation((a, b) => a * b, "*",
    function(name, a, b) { return new Add(
        new Multiply(a.diff(name), b), new Multiply(a, b.diff(name)))});

const Divide = createOperation((a, b) => a / b, "/",
    function(name, a, b) { return new Divide(
        new Subtract(new Multiply(a.diff(name), b), new Multiply(a, b.diff(name))),
        new Multiply(b, b))});

const Gauss = createOperation((a, b, c, x) => a * Math.exp(-(x - b) * (x - b) / (2 * c * c)), "gauss",
    function(name, a, b, c, x) { return new Add(
        new Multiply(a.diff(name), new Gauss(Const.ONE, b, c, x)),
        new Multiply(
            a, new Multiply(
                new Gauss(Const.ONE, b, c, x), Negate.prototype._diffRule(
                    name, new Divide(
                        new Multiply(new Subtract(x, b), new Subtract(x, b)),
                        new Multiply(Const.TWO, new Multiply(c, c)))))))});

const tokenToOperation = {
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
        if (token in tokenToOperation) {
            const operation = tokenToOperation[token];
            stack.push(new operation(...stack.splice(-operation.prototype._op.length)));
        } else if (token in varIndex) {
            stack.push(new Variable(token));
        } else {
            stack.push(new Const(token));
        }
    }
    return stack.pop();
}