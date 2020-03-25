"use strict";

const cnst = value => () => +value;

const one = cnst(1);
const two = cnst(2);
const tokenToConst = {
    "one": one,
    "two": two
};

const varIndex = {
    "x" : 0,
    "y" : 1,
    "z" : 2
};
const variable = name => (...vars) => vars[varIndex[name]];

const operation = (op, ...args) => (...vars) => op(...args.map(val => val(...vars)));

const negate = x => operation(x => -x, x);
const abs = x => operation(Math.abs, x);

const multiply = (a, b) => operation((a, b) => a * b, a, b);
const subtract = (a, b) => operation((a, b) => a - b, a, b);
const add = (a, b) => operation((a, b) => a + b, a, b);
const divide = (a, b) => operation((a, b) => a / b, a, b);

const iff = (a, b, c) => operation((a, b, c) => a >= 0 ? b : c, a, b, c);

const tokenToOperation = {
    "negate": negate,
    "abs": abs,
    "+": add,
    "-": subtract,
    "*": multiply,
    "/": divide,
    "iff": iff
};

function parse (expression) {
    let stack = [];
    const parseToken = token => {
        if (token in tokenToOperation) {
            stack.push(tokenToOperation[token](...stack.splice(-tokenToOperation[token].length)));
        } else if (token in varIndex) {
            stack.push(variable(token));
        } else if (token in tokenToConst) {
            stack.push(tokenToConst[token]);
        } else {
            stack.push(cnst(token));
        }
    };
    expression.trim().split(/\s+/).forEach(parseToken);
    return stack.pop();
}
