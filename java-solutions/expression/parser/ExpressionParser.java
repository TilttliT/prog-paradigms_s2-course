package expression.parser;

import expression.calculators.Calculator;
import expression.exceptions.*;
import expression.generic.ArithmeticExpression;
import expression.generic.*;

import java.util.Map;

public class ExpressionParser<T> {
    public ArithmeticExpression<T> parse(Calculator<T> calculator, String expression) throws ParserException {
        return parse(calculator, new StringSource(expression));
    }

    public ArithmeticExpression<T> parse(Calculator<T> calculator, final CharSource source) throws ParserException {
        return new Parser(calculator, source).parse();
    }

    private class Parser extends BaseParser {
        private final Map<String, BinaryConstructor<T>> operations;
        private final Calculator<T> calculator;
        private static final int[] priorityList;
        private int balance = 0;
        static {
            priorityList = new int[] {30, 20, 10, 0};
        }

        public Parser(final Calculator<T> calculator, final CharSource source) {
            super(source);
            this.calculator = calculator;
            operations = Map.of(
                    "*", Multiply::new,
                    "/", Divide::new,
                    "+", Add::new,
                    "-", Subtract::new,
                    "min", Min::new,
                    "max", Max::new
            );
        }

        public ArithmeticExpression<T> parse() throws ParserException {
            ArithmeticExpression<T> temp = expression(0);
            if (!eof() || balance > 0) {
                throw new IllegalBracketsException(getPos());
            }
            return temp;
        }

        private String parseAddSub () {
            if (take('+')) {
                return "+";
            } else if (take('-')) {
                return "-";
            }
            return "";
        }

        private String parseMulDiv () {
            if (take('*')) {
                return "*";
            } else if (take('/')) {
                return "/";
            }
            return "";
        }

        private String parseMinMax() throws ParserException {
            if (take('m')) {
                if (take('i')) {
                    expect('n');
                    return "min";
                } else if (take('a')) {
                    expect('x');
                    return "max";
                }
                throw new IllegalSymbolException("m", getPos(), "min or max");
            }
            return "";
        }

        private String parseOperation (int priority) throws ParserException {
            return switch (priority) {
                case 30 -> parseMinMax();
                case 20 -> parseAddSub();
                case 10 -> parseMulDiv();
                default -> throw new IllegalPriorityException(priority);
            };
        }

        private ArithmeticExpression<T> parseConst (StringBuilder number) throws ParserException {
            takeDigits(number);
            try {
                return new Const<>(calculator.parseConst(number.toString()));
            } catch (ParserException e) {
                throw new IllegalConstException(getPos(), number.toString());
            }
        }

        private ArithmeticExpression<T> parseFact () throws ParserException {
            ArithmeticExpression<T> result;
            skipWhitespace();
            boolean negate = take('-');
            if (take('(')) {
                ++balance;
                skipWhitespace();
                result = expression(0);
                if (negate) result = new Negate<>(calculator, result);
                skipWhitespace();
                expect(')');
                --balance;
                if (balance < 0) {
                    throw new IllegalBracketsException(getPos());
                }
            } else if (between('0', '9')) {
                StringBuilder number = new StringBuilder();
                if (negate) number.append('-');
                result = parseConst(number);
            } else if (between('x', 'z')) {
                result = new Variable<>(String.valueOf(take()));
            } else if (take('c')) {
                expect("ount");
                expectBracketsAndSkipWhitespace();
                result = new Count<>(calculator, expression(priorityList.length - 1));
            } else {
                throw new IllegalSymbolException(
                        String.valueOf(getCurrentChar()), getPos(),
                        "var, const, unaryOperation or (expression)"
                );
            }

            return result;
        }

        private ArithmeticExpression<T> expression(int depth) throws ParserException {
            ArithmeticExpression<T> result;

            if (depth != priorityList.length - 1) {
                result = expression(depth + 1);

                skipWhitespace();
                while (!eof()) {
                    String operator = parseOperation(priorityList[depth]);
                    if (operator.equals("")) {
                        break;
                    }
                    skipWhitespace();

                    ArithmeticExpression<T> temp = expression(depth + 1);
                    skipWhitespace();

                    result = operations.get(operator).create(calculator, result, temp);
                }
            } else {
                result = parseFact();
            }

            return result;
        }

        private void takeDigits(final StringBuilder sb) {
            while (between('0', '9')) {
                sb.append(take());
            }
        }
    }
}