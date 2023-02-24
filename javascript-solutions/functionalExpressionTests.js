"use strict"
include("functionalExpression.js")

let str = "x x * 2 x * - 1 +"
let expr1 = add(
    subtract(
        multiply(
            variable("x"),
            variable("x")
        ),
        multiply(
            cnst(2),
            variable("x")
        )
    ),
    cnst(1)
);
let expr2 = parse(str);

let correct = true;
for (let x = 0; x <= 10; ++x) {
    let ans1 = expr1(x, 0, 0), ans2 = expr2(x, 0, 0);
    if (ans1 !== ans2) {
        print("Wrong value! ")
        correct = false;
    }
    println("Expr1: " + ans1 + " Expr2: " + ans2);
}

if (correct === true) {
    println("Correct");
} else {
    println("Incorrect");
}