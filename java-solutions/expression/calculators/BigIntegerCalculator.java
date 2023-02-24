package expression.calculators;

import expression.exceptions.CalculationException;
import expression.exceptions.DBZException;

import java.math.BigInteger;

public class BigIntegerCalculator implements Calculator<BigInteger> {
    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger divide(BigInteger a, BigInteger b) throws CalculationException {
        if (!b.equals(BigInteger.ZERO)) {
            return a.divide(b);
        } else {
            throw new DBZException(a.toString(), b.toString());
        }
    }

    @Override
    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger parseConst(String constString) {
        return new BigInteger(constString);
    }

    @Override
    public BigInteger parseConst(int constInt) {
        return BigInteger.valueOf(constInt);
    }

    @Override
    public BigInteger min(BigInteger a, BigInteger b) {
        return a.min(b);
    }

    @Override
    public BigInteger max(BigInteger a, BigInteger b) {
        return a.max(b);
    }

    @Override
    public BigInteger count(BigInteger a) {
        return BigInteger.valueOf(a.bitCount());
    }

    @Override
    public BigInteger negate(BigInteger a) {
        return a.negate();
    }
}