package expression.exceptions;

import expression.ArithmeticExpression;
import expression.Pow;

public class CheckedPow extends Pow {
    public CheckedPow(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int a, int b) throws CalculationException {
        if (b < 0 || a == 0 && b == 0) {
            throw new IllegalPowArgumentsException(a, b);
        } else {
            int res = 1;
            int aSave = a, bSave = b;
            while (b > 0) {
                if (b % 2 == 1) {
                    if (CheckedMultiply.checkOverflow(res, a)) {
                        throw new OverflowException(getOperationSymbol(), aSave, bSave);
                    }
                    res *= a;
                }
                b /= 2;
                if (b != 0) {
                    if (CheckedMultiply.checkOverflow(a, a)) {
                        throw new OverflowException(getOperationSymbol(), aSave, bSave);
                    }
                    a *= a;
                }
            }
            return res;
        }
    }
}