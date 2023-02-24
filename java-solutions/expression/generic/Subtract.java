package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public class Subtract<T> extends BinaryOperation<T> implements NotAssociativityOperations<T> {
    public Subtract(Calculator<T> calculator, ArithmeticExpression<T> left, ArithmeticExpression<T> right) {
        super(calculator, left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public T makeOperation(T a, T b) throws CalculationException {
        return calculator.subtract(a, b);
    }
}