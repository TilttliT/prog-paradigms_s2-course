"use strict";

const variablesToIndex = {
    "x": 0,
    "y": 1,
    "z": 2
};
const variable = v => (...args) => args[variablesToIndex[v]];
const cnst = a => Object.defineProperty(
    () => a,
    "numOfOperands",
    {
        value: 0
    }
);
const operation = f => Object.defineProperty(
    (...operands) => (...args) => f(...operands.map(op => op(...args))),
    "numOfOperands",
    {
        value: f.length
    }
);

const add = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide = operation((a, b) => a / b);
const negate = operation(a => -a);
const avg3 = operation((a, b, c) => (a + b + c) / 3);
const med5 = operation((a1, a2, a3, a4, a5) => [a1, a2, a3, a4, a5].sort((x, y) => x - y)[2]);
const pi = cnst(Math.PI);
const e = cnst(Math.E);

const tokenToExpression = {
    "+": add,
    "-": subtract,
    "*": multiply,
    "/": divide,
    "negate": negate,
    "avg3": avg3,
    "med5": med5,
    "pi": pi,
    "e": e
};
const parse = (expression) => {
    const stack = [];
    for (const token of expression.split(" ")) {
        if (token in variablesToIndex) {
            stack.push(variable(token));
        } else if (token in tokenToExpression) {
            const op = tokenToExpression[token];
            const num = op.numOfOperands;
            stack.push(num !== 0 ? op(...stack.splice(-num, num)) : op);
        } else if (token.length !== 0) {
            stack.push(cnst(parseInt(token)));
        }
    }
    return stack.pop();
}