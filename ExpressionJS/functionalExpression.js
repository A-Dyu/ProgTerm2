"use strict";

const cnst = value => x => value;

const variable = name => x => x;

const operation = op => (a, b) => x => op(a(x), b(x));
const multiply = operation((a, b) => a * b);
const subtract = operation((a, b) => a - b);
const add = operation((a, b) => a + b);
const divide = operation((a, b) => a / b);

let symbolToOperation = {};
symbolToOperation['+'] = add;
symbolToOperation["-"] = subtract;
symbolToOperation["*"] = multiply;
symbolToOperation["/"] = divide;

function parse (expression) {
    let stack = [];
    expression.split(" ").filter(s => s.trim() !== "").forEach(parseToken);
    function parseToken(token) {
        if (symbolToOperation[token] !== undefined) {
            let b = stack.pop(), a = stack.pop();
            stack.push(symbolToOperation[token](a, b));
        } else if (token === "x") {
            stack.push(variable(token));
        } else {
            stack.push(cnst(+token));
        }
    }
    return stack.pop();
}