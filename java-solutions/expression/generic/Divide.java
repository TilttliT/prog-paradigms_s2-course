package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public class Divide<T> extends BinaryOperation<T> implements NotAssociativityOperations<T> {
    public Divide(Calculator<T> calculator, ArithmeticExpression<T> left, ArithmeticExpression<T> right) {
        super(calculator, left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public T makeOperation(T a, T b) throws CalculationException {
        return calculator.divide(a, b);
    }
}