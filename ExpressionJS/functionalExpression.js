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

const operation = op => (...args) => (...vars) => op(...args.map(val => val(...vars)));

const negate = operation(x => -x);
const abs = operation(Math.abs);

const multiply = operation((a, b) => a * b);
const subtract = operation((a, b) => a - b);
const add = operation((a, b) => a + b);
const divide = operation((a, b) => a / b);

const iff = operation((a, b, c) => a >= 0 ? b : c);

const tokenToOperation = {
    "negate": negate,
    "abs": abs,
    "+": add,
    "-": subtract,
    "*": multiply,
    "/": divide,
    "iff": iff
};

const argsAmount = {
    "negate": 1,
    "abs": 1,
    "+": 2,
    "-": 2,
    "*": 2,
    "/": 2,
    "iff": 3
};


function parse (expression) {
    let stack = [];
    const parseToken = token => {
        if (token in tokenToOperation) {
            stack.push(tokenToOperation[token](...(stack.splice(-argsAmount[token]))));
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
