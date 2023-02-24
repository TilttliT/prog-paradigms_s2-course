package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public class Multiply<T> extends BinaryOperation<T> {
    public Multiply(Calculator<T> calculator, ArithmeticExpression<T> left, ArithmeticExpression<T> right) {
        super(calculator, left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public T makeOperation(T a, T b) throws CalculationException {
        return calculator.multiply(a, b);
    }
}