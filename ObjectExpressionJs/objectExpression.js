"use strict";

function Const(value) {
    this.value = value;
}
Const.prototype.evaluate = function() { return +this.value};
Const.prototype.toString = function() { return "" + this.value};
Const.prototype.diff = function() { return new Const(0)};

const varIndex = {
    "x": 0,
    "y": 1,
    "z": 2
};
function Variable(name) {
    this.name = name;
}
Variable.prototype.evaluate = function(...vars) { return vars[varIndex[this.name]]};
Variable.prototype.toString = function() { return this.name};
Variable.prototype.diff = function(name) { return this.name === name ? new Const(1) : new Const(0)};


function Operation(...args) {
    this.args = args;
}
Operation.prototype.evaluate = function(...vars) { return this.op(...this.args.map(val => val.evaluate(...vars)))};
Operation.prototype.toString = function() { return this.args.slice().reduce((total, v) => total + " " + v.toString()) + " " + this.operator};

function Negate(x) {
    Operation.call(this, x);
}
Negate.prototype = Object.create(Operation.prototype);
Negate.prototype.op = x => -x;
Negate.prototype.operator = "negate";
Negate.prototype.diff = function(name) { return new Negate(this.args[0].diff(name))};

function Add(x, y) {
    Operation.call(this, x, y);
}
Add.prototype = Object.create(Operation.prototype);
Add.prototype.op = (a, b) => a + b;
Add.prototype.operator = "+";
Add.prototype.diff = function(name) { return new Add(this.args[0].diff(name), this.args[1].diff(name))};

function Subtract(x, y) {
    Operation.call(this, x, y);
}
Subtract.prototype = Object.create(Operation.prototype);
Subtract.prototype.op = (a, b) => a - b;
Subtract.prototype.operator = "-";
Subtract.prototype.diff = function(name) { return new Subtract(this.args[0].diff(name), this.args[1].diff(name))};

function Multiply(x, y) {
    Operation.call(this, x, y);
}
Multiply.prototype = Object.create(Operation.prototype);
Multiply.prototype.op = (a, b) => a * b;
Multiply.prototype.operator = "*";
Multiply.prototype.diff = function(name) { return new Add(
    new Multiply(this.args[0].diff(name), this.args[1]), new Multiply(this.args[0], this.args[1].diff(name)))};

function Divide(x, y) {
    Operation.call(this, x, y);
}
Divide.prototype = Object.create(Operation.prototype);
Divide.prototype.op = (a, b) => a / b;
Divide.prototype.operator = "/";
Divide.prototype.diff = function(name) { return new Divide(
    new Subtract(new Multiply(this.args[0].diff(name), this.args[1]), new Multiply(this.args[0], this.args[1].diff(name))),
    new Multiply(this.args[1], this.args[1])
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