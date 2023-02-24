package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

import java.util.Map;

public class ExpressionParser implements TripleParser {
    @Override
    public ArithmeticExpression parse(String expression) throws ParserException {
        return parse(new StringSource(expression));
    }

    public static ArithmeticExpression parse(final CharSource source) throws ParserException {
        return new Parser(source).parse();
    }

    private static class Parser extends BaseParser {
        private static final Map<String, BinaryConstructor> operations;
        private static final int[] priorityList;
        private int balance = 0;

        static {
            priorityList = new int[]{30, 20, 10, 5, 0};
            operations = Map.of(
                    "*", CheckedMultiply::new,
                    "/", CheckedDivide::new,
                    "+", CheckedAdd::new,
                    "-", CheckedSubtract::new,
                    "<<", LeftShift::new,
                    ">>", RightShift::new,
                    ">>>", ArithmeticRightShift::new,
                    "**", CheckedPow::new,
                    "//", CheckedLog::new
            );
        }

        @FunctionalInterface
        interface BinaryConstructor {
            ArithmeticExpression create(ArithmeticExpression left, ArithmeticExpression right);
        }

        public Parser(final CharSource source) {
            super(source);
        }

        public ArithmeticExpression parse() throws ParserException {
            ArithmeticExpression temp = expression(0);
            if (!eof() || balance > 0) {
                throw new IllegalBracketsException(getPos());
            }
            return temp;
        }

        private String parseShifts() throws ParserException {
            if (take('<')) {
                expect('<');
                return "<<";
            } else if (take('>')) {
                expect('>');
                if (take('>')) {
                    return ">>>";
                } else {
                    return ">>";
                }
            }
            return "";
        }

        private String parseAddSub() {
            if (take('+')) {
                return "+";
            } else if (take('-')) {
                return "-";
            }
            return "";
        }

        private String parseMulDiv() {
            if (take('*')) {
                return "*";
            } else if (take('/')) {
                return "/";
            }
            return "";
        }

        private String parsePowLog() {
            if (take('*')) {
                if (take('*')) {
                    return "**";
                } else {
                    back();
                }
            } else if (take('/')) {
                if (take('/')) {
                    return "//";
                } else {
                    back();
                }
            }
            return "";
        }

        private String parseOperation(int priority) throws ParserException {
            return switch (priority) {
                case 30 -> parseShifts();
                case 20 -> parseAddSub();
                case 10 -> parseMulDiv();
                case 5 -> parsePowLog();
                default -> throw new IllegalPriorityException(priority);
            };
        }

        private static final String constMax = Integer.valueOf(Integer.MAX_VALUE).toString();
        private static final String constMin = Integer.valueOf(Integer.MIN_VALUE).toString();

        private boolean MinMaxComparator(String number) {
            if (number.charAt(0) == '-') {
                return number.length() > constMin.length() ||
                        number.length() == constMin.length() &&
                                number.compareTo(constMin) > 0;
            } else {
                return number.length() > constMax.length() ||
                        number.length() == constMax.length() &&
                                number.compareTo(constMax) > 0;
            }
        }

        private ArithmeticExpression parseConst(StringBuilder number) throws IllegalConstException {
            takeDigits(number);
            if (!MinMaxComparator(number.toString())) {
                return new Const(Integer.parseInt(number.toString()));
            } else {
                throw new IllegalConstException(getPos(), number.toString());
            }
        }

        private ArithmeticExpression parseFact() throws ParserException {
            ArithmeticExpression result;
            skipWhitespace();
            if (take('(')) {
                ++balance;
                skipWhitespace();
                result = expression(0);
                skipWhitespace();
                expect(')');
                --balance;
                if (balance < 0) {
                    throw new IllegalBracketsException(getPos());
                }
            } else if (between('0', '9')) {
                result = parseConst(new StringBuilder());
            } else if (take('-')) {
                if (between('0', '9')) {
                    StringBuilder number = new StringBuilder();
                    number.append('-');
                    result = parseConst(number);
                } else {
                    result = new CheckedNegate(expression(priorityList.length - 1));
                }
            } else if (between('x', 'z')) {
                result = new Variable(String.valueOf(take()));
            } else if (take('a')) {
                expect("bs");
                expectBracketsAndSkipWhitespace();
                result = new CheckedAbs(expression(priorityList.length - 1));
            } else {
                throw new IllegalSymbolException(String.valueOf(getCurrentChar()), getPos(), "var, const, unaryOperation or (expression)");
            }

            return result;
        }

        private ArithmeticExpression expression(int depth) throws ParserException {
            ArithmeticExpression result;

            if (depth != priorityList.length - 1) {
                result = expression(depth + 1);

                skipWhitespace();
                while (!eof()) {
                    String operator = parseOperation(priorityList[depth]);
                    if (operator.equals("")) {
                        break;
                    }
                    skipWhitespace();

                    ArithmeticExpression temp = expression(depth + 1);
                    skipWhitespace();

                    result = operations.get(operator).create(result, temp);
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