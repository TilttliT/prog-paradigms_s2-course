package expression.calculators;

import expression.exceptions.*;

public class IntegerCalculator implements Calculator<Integer> {
    private final boolean checkOverflow;

    public IntegerCalculator(boolean checkOverflow) {
        this.checkOverflow = checkOverflow;
    }

    protected Integer modifyValue(Integer a) {
        return a;
    }

    private boolean checkAddOverflow(Integer a, Integer b) {
        return a >= 0 && Integer.MAX_VALUE - a < b ||
                a <= 0 && Integer.MIN_VALUE - a > b;
    }

    @Override
    public Integer add(Integer a, Integer b) throws CalculationException {
        if (checkOverflow && checkAddOverflow(a, b)) {
            throw new OverflowException("+", a, b);
        } else {
            return modifyValue(a + b);
        }
    }

    private boolean checkMultiplyOverflow(int a, int b) {
        if (a > 0 && b > 0) {
            return a > Integer.MAX_VALUE / b;
        } else if (a < 0 && b < 0) {
            return b < Integer.MAX_VALUE / a;
        } else if (a < 0 && b > 0) {
            return a < Integer.MIN_VALUE / b;
        } else if (a > 0 && b < 0) {
            return b < Integer.MIN_VALUE / a;
        }
        return false;
    }

    @Override
    public Integer multiply(Integer a, Integer b) throws CalculationException {
        if (checkOverflow && checkMultiplyOverflow(a, b)) {
            throw new OverflowException("*", a, b);
        } else {
            return modifyValue(a * b);
        }
    }

    @Override
    public Integer divide(Integer a, Integer b) throws CalculationException {
        if (b != 0) {
            return modifyValue(a / b);
        } else {
            throw new DBZException(a.toString(), b.toString());
        }
    }

    private boolean checkSubtractOverflow(Integer a, Integer b) {
        return b <= 0 && Integer.MAX_VALUE + b < a ||
                b >= 0 && Integer.MIN_VALUE + b > a;
    }

    @Override
    public Integer subtract(Integer a, Integer b) throws CalculationException {
        if (checkOverflow && checkSubtractOverflow(a, b)) {
            throw new OverflowException("-", a, b);
        } else {
            return modifyValue(a - b);
        }
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

    @Override
    public Integer parseConst(String constString) throws ParserException {
        if (checkOverflow && MinMaxComparator(constString)) {
            throw new IllegalConstException("");
        } else {
            return modifyValue(Integer.parseInt(constString));
        }
    }

    private boolean checkNegateOverflow(Integer a) {
        return a == Integer.MIN_VALUE;
    }

    @Override
    public Integer negate(Integer a) {
        if (checkOverflow && checkNegateOverflow(a)) {
            throw new OverflowException("-", a);
        } else {
            return -a;
        }
    }

    @Override
    public Integer parseConst(int constInt) {
        return modifyValue(constInt);
    }

    @Override
    public Integer min(Integer a, Integer b) {
        return Integer.min(a, b);
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return Integer.max(a, b);
    }

    @Override
    public Integer count(Integer a) {
        return modifyValue(Integer.bitCount(a));
    }
}