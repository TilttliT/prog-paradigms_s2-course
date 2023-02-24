package expression.exceptions;

import expression.Add;
import expression.ArithmeticExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int a, int b) throws CalculationException {
        if (a >= 0 && Integer.MAX_VALUE - a >= b ||
                a <= 0 && Integer.MIN_VALUE - a <= b) {

            return super.makeOperation(a, b);
        } else {
            throw new OverflowException(getOperationSymbol(), a, b);
        }
    }
}