"use strict";

const cnst = (value) => (x) => value;

const variable = (name) => (x) => x;

const operation = (op) => (a, b) => (x) => op(a(x), b(x));
const multiply = operation((a, b) => (a * b));
const subtract = operation((a, b) => (a - b));
const add = operation((a, b) => (a + b));
const divide = operation((a, b) => (a / b));

function parse (expression) {
    let stack = [];
    for (let i = 0; i < expression.length; i++) {
        while (i < expression.length && expression[i] === " ") {
            i++;
        }
        if (i >= expression.length) {
            break;
        }
        switch (expression[i]) {
            case "-":
                if (i < expression.length - 1 && isDigit(expression[i + 1])) {
                    i++;
                    stack.push(cnst(-parseNumber()));
                } else {
                    stack.push(makeOperation(stack, subtract));
                }
                break;
            case "+":
                stack.push(makeOperation(stack, add));
                break;
            case "*":
                stack.push(makeOperation(stack, multiply));
                break;
            case "/":
                stack.push(makeOperation(stack, divide));
                break;
            case "x":
                stack.push(variable("x"));
                break;
            default:
                stack.push(cnst(parseNumber()));
        }
        function parseNumber() {
            let c = 0;
            while (i < expression.length && isDigit(expression[i])) {
                c *= 10;
                c += +expression[i];
                i++;
            }
            return c;
        }
    }
    return stack[0];
}

function makeOperation(stack, operation){
    let b = stack.pop(), a = stack.pop();
    return operation(a, b);
}

const isDigit = x => x >= "0" && x <= "9";