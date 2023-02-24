package expression.exceptions;

import expression.ArithmeticExpression;
import expression.Log;

public class CheckedLog extends Log {
    public CheckedLog(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int a, int b) throws CalculationException {
        if (a <= 0 || b <= 0 || b == 1) {
            throw new IllegalLogArgumentsException(a, b);
        } else {
            return super.makeOperation(a, b);
        }
    }
}