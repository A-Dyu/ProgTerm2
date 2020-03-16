"use strict";

const cnst = (value) => (x) => value;

const variable = (name) => (x) => x;

const operation = (op) => (a, b) => (x) => op(a(x), b(x));
const multiply = operation((a, b) => (a * b));
const subtract = operation((a, b) => (a - b));
const add = operation((a, b) => (a + b));
const divide = operation((a, b) => (a / b));

