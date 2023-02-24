package expression.exceptions;

import expression.ArithmeticExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    public static boolean checkOverflow(int a, int b) {
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
    public int makeOperation(int a, int b) throws CalculationException {
        if (checkOverflow(a, b)) {
            throw new OverflowException(getOperationSymbol(), a, b);
        } else {
            return super.makeOperation(a, b);
        }
    }
}