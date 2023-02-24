"use strict";

const variablesToIndex = {
    "x": 0,
    "y": 1,
    "z": 2,
};

function makeExpression(expression, evaluate, toString, diff, prefix, postfix) {
    expression.prototype.evaluate = evaluate;
    expression.prototype.toString = toString;
    expression.prototype.diff = diff;
    expression.prototype.prefix = prefix === undefined ? toString : prefix;
    expression.prototype.postfix = postfix === undefined ? toString : postfix;
}

function AbstractOperation(...operands) {
    this.operands = operands;
}

makeExpression(
    AbstractOperation,
    function (...args) {
        return this.func(...this.operands.map(op => op.evaluate(...args)));
    },
    function () {
        return this.operands.map(op => op.toString()).join(' ') + " " + this.sign;
    },
    function (name) {
        return this.diffRule(name, ...this.operands);
    },
    function () {
        return "(" + this.sign + " " + this.operands.map(op => op.prefix()).join(" ") + ")";
    },
    function () {
        return "(" + this.operands.map(op => op.postfix()).join(" ") + " " + this.sign + ")";
    }
)
const Const = function (val) {
    this.val = val;
}
makeExpression(
    Const,
    function () {
        return this.val;
    },
    function () {
        return this.val.toString();
    },
    function () {
        return ZERO;
    }
)
const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);
const Variable = function (name) {
    this.name = name;
}
makeExpression(
    Variable,
    function (...args) {
        return args[variablesToIndex[this.name]];
    },
    function () {
        return this.name;
    },
    function (name) {
        return this.name === name ? ONE : ZERO;
    }
)

function makeOperation(func, sign, diffRule) {
    const Operation = function (...operands) {
        AbstractOperation.apply(this, operands);
    }
    Operation.prototype = Object.create(AbstractOperation.prototype);
    Operation.prototype.constructor = Operation;
    Operation.prototype.func = func;
    Operation.prototype.sign = sign;
    Operation.prototype.diffRule = diffRule;
    return Operation;
}

const Add = makeOperation((a, b) => a + b, "+",
    (name, a, b) => new Add(a.diff(name), b.diff(name)));
const Subtract = makeOperation((a, b) => a - b, "-",
    (name, a, b) => new Subtract(a.diff(name), b.diff(name)));
const Multiply = makeOperation((a, b) => a * b, "*",
    (name, a, b) => new Add(new Multiply(a.diff(name), b), new Multiply(a, b.diff(name))));
const Divide = makeOperation((a, b) => a / b, "/", (name, a, b) =>
    new Divide(
        new Subtract(new Multiply(a.diff(name), b), new Multiply(a, b.diff(name))),
        new Multiply(b, b)
    )
);
const Negate = makeOperation(a => -a, "negate",
    (name, a) => new Negate(a.diff(name)));
const Gauss = makeOperation((a, b, c, x) => a * Math.exp(-(x - b) * (x - b) / (2 * c * c)), "gauss",
    (name, a, b, c, x) => new Add(
        new Gauss(a.diff(name), b, c, x),
        new Multiply(
            new Gauss(a, b, c, x),
            new Divide(
                new Negate(new Multiply(new Subtract(x, b), new Subtract(x, b))),
                new Multiply(TWO, new Multiply(c, c))
            ).diff(name)
        )
    )
);
const Sumexp = makeOperation(
    (...operands) => operands.reduce((previousValue, currentValue) => previousValue + Math.exp(currentValue), 0),
    "sumexp",
    (name, ...operands) => operands.reduce(
        (previousValue, currentValue) =>
            new Add(previousValue, new Multiply(currentValue.diff(name), new Sumexp(currentValue))),
        ZERO
    )
);
const Softmax = makeOperation(
    (...operands) => Math.exp(operands[0]) / Sumexp.prototype.func(...operands),
    "softmax",
    (name, ...operands) => new Multiply(
        new Softmax(...operands),
        new Subtract(
            operands[0].diff(name),
            new Divide(new Sumexp(...operands).diff(name), new Sumexp(...operands))
        )
    )
)

const tokenToExpression = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "gauss": Gauss,
    "sumexp": Sumexp,
    "softmax": Softmax,
};
const parse = (expression) => {
    const stack = [];
    for (const token of expression.split(" ")) {
        if (token in variablesToIndex) {
            stack.push(new Variable(token));
        } else if (token in tokenToExpression) {
            const op = tokenToExpression[token];
            const num = op.prototype.func.length;
            stack.push(new op(...stack.splice(-num, num)));
        } else if (token.length !== 0) {
            stack.push(new Const(parseInt(token)));
        }
    }
    return stack.pop();
}

//---------------------------------------------------------------------------------------------------------------
function ParseError(message) {
    this.message = message;
}

ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "ParseError";
ParseError.prototype.constructor = ParseError;

function makeParseError(name, message) {
    function NewError(...args) {
        ParseError.call(this, message(...args));
    }

    NewError.prototype = Object.create(ParseError.prototype);
    NewError.prototype.name = name;
    NewError.prototype.constructor = ParseError;
    return NewError;
}

const IllegalNameOfOperationError = makeParseError("IllegalNameOfOperation",
    (pos, name) => "In position " + pos + " find unknown name of operation: " + name);
const IllegalNameOfOperandError = makeParseError("IllegalNameOfOperand",
    (pos, name) => "In position " + pos + " find unknown name of operand: " + name);
const IllegalNumberOfOperandsError = makeParseError("IllegalNumberOfOperands",
    (pos, sign, findNum, expectedNum) =>
        "For operation " + sign + " in position " + pos +
        " expected " + expectedNum + " operands, but find " + findNum
);
const IllegalSymbolError = makeParseError("IllegalSymbolError",
    (pos, symbol) => "In position " + pos + " expected symbol " + symbol);
const ExpectedEOFError = makeParseError("ExpectedEOFError",
    pos => "In position " + pos + " expected end of expression")
const EmptyExpressionError = makeParseError("EmptyExpressionError",
    () => "Find empty expression")

const PARSERS = {PREFIX: 0, POSTFIX: 1};

function parser(expression, mode) {
    let _pos = 0;
    const WHITESPACES = [" "];
    const BRACKETS = ["(", ")"];
    let curToken = "";

    function eof() {
        return _pos === expression.length;
    }

    function skipWhitespace() {
        while (!eof() && WHITESPACES.includes(expression[_pos])) {
            _pos++;
        }
    }

    function hasNextToken() {
        skipWhitespace();
        return !eof();
    }

    function nextToken() {
        let result = curToken;
        curToken = "";
        if (result !== "") {
            return result;
        } else {
            skipWhitespace();
            let result = "";
            while (!eof() && !WHITESPACES.includes(expression[_pos]) && !BRACKETS.includes(expression[_pos])) {
                result += expression[_pos];
                _pos++;
            }
            if (result.length === 0 && !eof() && BRACKETS.includes(expression[_pos])) {
                result += expression[_pos];
                _pos++;
            }
            return result;
        }
    }

    function viewToken() {
        const result = nextToken();
        curToken = result;
        return result;
    }

    function parseOperation() {
        const token = nextToken();
        if (token in tokenToExpression) {
            return tokenToExpression[token];
        } else {
            throw new IllegalNameOfOperationError(_pos - token.length + 1, token);
        }
    }

    function parseOperand(token) {
        if (token === "(") {
            const result = parseExpression();
            if (nextToken() !== ")") {
                throw new IllegalSymbolError(_pos + 1, ")");
            }
            return result;
        } else if (token in variablesToIndex) {
            return new Variable(token);
        } else if (!isNaN(+token)) {
            return new Const(parseInt(token));
        } else {
            throw new IllegalNameOfOperandError(_pos - token.length + 1, token);
        }
    }

    function parseOperands() {
        let token = viewToken();
        const result = [];
        while (!eof() && !(token === ")" || token in tokenToExpression)) {
            result.push(parseOperand(nextToken()));
            token = viewToken();
        }
        return result;
    }

    function parseExpression() {
        const startPos = _pos;
        let operation, operands;
        if (mode === PARSERS.PREFIX) {
            operation = parseOperation();
            operands = parseOperands();
        } else {
            operands = parseOperands();
            operation = parseOperation();
        }

        const expectedLength = operation.prototype.func.length;
        const findLength = operands.length;
        if (expectedLength === 0 || expectedLength === findLength) {
            return new operation(...operands);
        } else {
            throw new IllegalNumberOfOperandsError(
                startPos + 1, operation.prototype.sign, findLength, expectedLength);
        }
    }

    function parse() {
        if (!hasNextToken()) {
            throw new EmptyExpressionError();
        }
        let result = parseOperand(nextToken());
        if (hasNextToken()) {
            throw new ExpectedEOFError(_pos + 1);
        }
        return result;
    }

    return parse();
}

const parsePrefix = (expression) => parser(expression, PARSERS.PREFIX);
const parsePostfix = (expression) => parser(expression, PARSERS.POSTFIX);
