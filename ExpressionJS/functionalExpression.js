"use strict";

const cnst = value => () => +value;

const one = cnst(1);
const two = cnst(2);
const tokenToConst = {
    one: one,
    two: two
};

const getVar = ind => (...vars) => vars[ind];
const varIndex = {
    x: getVar(0),
    y: getVar(1),
    z: getVar(2)
};
const variable = name => varIndex[name];

const operation = op => {
    let func = (...args) => (...vars) => op(...args.map(val => val(...vars)));
    func.argsCount = op.length;
    return func;
};

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

function parse (expression) {
    let stack = [];
    for (const token of expression.trim().split(/\s+/)) {
        if (token in tokenToOperation) {
            const operation = tokenToOperation[token];
            stack.push(operation(...stack.splice(-operation.argsCount)));
        } else if (token in varIndex) {
            stack.push(variable(token));
        } else if (token in tokenToConst) {
            stack.push(tokenToConst[token]);
        } else {
            stack.push(cnst(token));
        }
    }
    return stack.pop();
}
