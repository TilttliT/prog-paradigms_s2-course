package expression.exceptions;

import expression.ArithmeticExpression;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(ArithmeticExpression left) {
        super(left);
    }

    @Override
    public int makeOperation(int a) throws CalculationException {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException(getOperationSymbol(), a);
        } else {
            return super.makeOperation(a);
        }
    }
}