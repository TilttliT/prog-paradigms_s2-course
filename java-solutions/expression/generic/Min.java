package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public class Min<T> extends BinaryOperation<T> {
    public Min(Calculator<T> calculator, ArithmeticExpression<T> left, ArithmeticExpression<T> right) {
        super(calculator, left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "min";
    }

    @Override
    public int getPriority() {
        return 30;
    }

    @Override
    public T makeOperation(T a, T b) throws CalculationException {
        return calculator.min(a, b);
    }
}