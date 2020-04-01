"use strict";

function createExpression(_evaluate, _toString, _diff) {
    this.prototype.evaluate = _evaluate;
    this.prototype.toString = _toString;
    this.prototype.diff = _diff;
}

function Const(value) {
    this._value = value;
}
createExpression.call(Const,
    function() { return +this._value},
    function() { return "" + this._value},
    function() { return ZERO});

const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);

const varIndex = {
    "x": 0,
    "y": 1,
    "z": 2
};
function Variable(name) {
    this._name = name;
    this._ind = varIndex[name];
}
createExpression.call(Variable,
    function(...vars) { return vars[this._ind]},
    function() { return this._name},
    function(name) { return this._name === name ? ONE : ZERO});

function Operation(...args) {
    this._args = args;
}
createExpression.call(Operation,
    function(...vars) { return this._op(...this._args.map(val => val.evaluate(...vars)))},
    function() { return this._args.slice().reduce((total, v) => total + " " + v.toString()) + " " + this._operator},
    function (name) { return this._diffRule(name, ...this._args)});

function createOperation(_op, _operator, _diffRule) {
    this.prototype = Object.create(Operation.prototype);
    this.prototype._op = _op;
    this.prototype._operator = _operator;
    this.prototype._diffRule = _diffRule;
}

function Negate(x) {
    Operation.call(this, x);
}
createOperation.call(Negate, x => -x, "negate",
    function(name, x) { return new Negate(x.diff(name))});

function Add(x, y) {
    Operation.call(this, x, y);
}
createOperation.call(Add, (a, b) => a + b, "+",
    function(name, a, b) { return new Add(a.diff(name), b.diff(name))});

function Subtract(x, y) {
    Operation.call(this, x, y);
}
createOperation.call(Subtract, (a, b) => a - b, "-",
    function(name, a, b) { return new Subtract(a.diff(name), b.diff(name))});

function Multiply(x, y) {
    Operation.call(this, x, y);
}
createOperation.call(Multiply, (a, b) => a * b, "*",
    function(name, a, b) { return new Add(
        new Multiply(a.diff(name), b), new Multiply(a, b.diff(name)))});

function Divide(x, y) {
    Operation.call(this, x, y);
}
createOperation.call(Divide, (a, b) => a / b, "/",
    function(name, a, b) { return new Divide(
        new Subtract(new Multiply(a.diff(name), b), new Multiply(a, b.diff(name))),
        new Multiply(b, b))});

function Gauss(a, b, c, x) {
    Operation.call(this, a, b, c, x);
}
createOperation.call(Gauss, (a, b, c, x) => a * Math.exp(-(x - b) * (x - b) / (2 * c * c)), "gauss",
    function(name, a, b, c, x) { return new Add(
        new Multiply(a.diff(name), new Gauss(ONE, b, c, x)),
        new Multiply(
            a, new Multiply(
                new Gauss(ONE, b, c, x), Negate.prototype._diffRule(
                    name, new Divide(
                        new Multiply(new Subtract(x, b), new Subtract(x, b)),
                        new Multiply(TWO, new Multiply(c, c)))))))});

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
    const parseToken = token => {
        if (token in tokenToOperation) {
            stack.push(new tokenToOperation[token](...stack.splice(-tokenToOperation[token].length)));
        } else if (token in varIndex) {
            stack.push(new Variable(token));
        } else {
            stack.push(new Const(token));
        }
    };
    expression.trim().split(/\s+/).forEach(parseToken);
    return stack.pop();
}
