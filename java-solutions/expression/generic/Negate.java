package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public class Negate<T> extends UnaryOperation<T> {
    public Negate(Calculator<T> calculator, ArithmeticExpression<T> left) {
        super(calculator, left);
    }

    @Override
    public String getOperationSymbol() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    protected T makeOperation(T a) throws CalculationException {
        return calculator.negate(a);
    }
}