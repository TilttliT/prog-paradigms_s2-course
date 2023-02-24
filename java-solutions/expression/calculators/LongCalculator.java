package expression.calculators;

import expression.exceptions.CalculationException;
import expression.exceptions.DBZException;

public class LongCalculator implements Calculator<Long> {
    @Override
    public Long add(Long a, Long b) {
        return a + b;
    }

    @Override
    public Long multiply(Long a, Long b) {
        return a * b;
    }

    @Override
    public Long divide(Long a, Long b) throws CalculationException {
        if (b != 0) {
            return a / b;
        } else {
            throw new DBZException(a.toString(), b.toString());
        }
    }

    @Override
    public Long subtract(Long a, Long b) {
        return a - b;
    }

    @Override
    public Long parseConst(String constString) {
        return Long.parseLong(constString);
    }

    @Override
    public Long parseConst(int constInt) {
        return (long) constInt;
    }

    @Override
    public Long min(Long a, Long b) {
        return Long.min(a, b);
    }

    @Override
    public Long max(Long a, Long b) {
        return Long.max(a, b);
    }

    @Override
    public Long count(Long a) {
        return (long) Long.bitCount(a);
    }

    @Override
    public Long negate(Long a) {
        return -a;
    }
}