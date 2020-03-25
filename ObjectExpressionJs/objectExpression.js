"use strict";

function Const(value) {
    this._value = value;
}
Const.prototype.evaluate = function() { return +this._value};
Const.prototype.toString = function() { return "" + this._value};
Const.prototype.diff = function() { return new Const(0)};

const varIndex = {
    "x": 0,
    "y": 1,
    "z": 2
};
function Variable(name) {
    this._name = name;
}
Variable.prototype.evaluate = function(...vars) { return vars[varIndex[this._name]]};
Variable.prototype.toString = function() { return this._name};
Variable.prototype.diff = function(name) { return new Const(this._name === name ? 1 : 0)};


function Operation(...args) {
    this._args = args;
}
Operation.prototype.evaluate = function(...vars) { return this._op(...this._args.map(val => val.evaluate(...vars)))};
Operation.prototype.toString = function() { return this._args.slice().reduce((total, v) => total + " " + v.toString()) + " " + this._operator};
Operation.prototype.diff = function (name) { return this._diffRule(name, ...this._args) };

function Negate(x) {
    Operation.call(this, x);
}
Negate.prototype = Object.create(Operation.prototype);
Negate.prototype._op = x => -x;
Negate.prototype._operator = "negate";
Negate.prototype._diffRule = function(name, x) { return new Negate(x.diff(name))};

function Add(x, y) {
    Operation.call(this, x, y);
}
Add.prototype = Object.create(Operation.prototype);
Add.prototype._op = (a, b) => a + b;
Add.prototype._operator = "+";
Add.prototype._diffRule = function(name, a, b) { return new Add(a.diff(name), b.diff(name))};

function Subtract(x, y) {
    Operation.call(this, x, y);
}
Subtract.prototype = Object.create(Operation.prototype);
Subtract.prototype._op = (a, b) => a - b;
Subtract.prototype._operator = "-";
Subtract.prototype._diffRule = function(name, a, b) { return new Subtract(a.diff(name), b.diff(name))};

function Multiply(x, y) {
    Operation.call(this, x, y);
}
Multiply.prototype = Object.create(Operation.prototype);
Multiply.prototype._op = (a, b) => a * b;
Multiply.prototype._operator = "*";
Multiply.prototype._diffRule = function(name, a, b) { return new Add(
    new Multiply(a.diff(name), b), new Multiply(a, b.diff(name)))};

function Divide(x, y) {
    Operation.call(this, x, y);
}
Divide.prototype = Object.create(Operation.prototype);
Divide.prototype._op = (a, b) => a / b;
Divide.prototype._operator = "/";
Divide.prototype._diffRule = function(name, a, b) { return new Divide(
    new Subtract(new Multiply(a.diff(name), b), new Multiply(a, b.diff(name))),
    new Multiply(b, b)
)};

const tokenToOperation = {
    "negate": Negate,
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide
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
