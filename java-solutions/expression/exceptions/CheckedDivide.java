package expression.exceptions;

import expression.ArithmeticExpression;
import expression.Divide;

public class CheckedDivide extends Divide {
    public CheckedDivide(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int a, int b) throws CalculationException {
        if (b != 0) {
            if (b == -1 && a == Integer.MIN_VALUE) {
                throw new OverflowException(getOperationSymbol(), a, b);
            }
            return super.makeOperation(a, b);
        } else {
            throw new DBZException(Integer.toString(a), Integer.toString(b));
        }
    }
}