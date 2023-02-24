package expression.exceptions;

import expression.ArithmeticExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int a, int b) throws CalculationException {
        if (b <= 0 && Integer.MAX_VALUE + b >= a ||
                b >= 0 && Integer.MIN_VALUE + b <= a) {

            return super.makeOperation(a, b);
        } else {
            throw new OverflowException(getOperationSymbol(), a, b);
        }
    }
}