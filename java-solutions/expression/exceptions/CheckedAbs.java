package expression.exceptions;

import expression.Abs;
import expression.ArithmeticExpression;

public class CheckedAbs extends Abs {
    public CheckedAbs(ArithmeticExpression left) {
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