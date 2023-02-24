package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public class Count<T> extends UnaryOperation<T> {
    public Count(Calculator<T> calculator, ArithmeticExpression<T> left) {
        super(calculator, left);
    }

    @Override
    public String getOperationSymbol() {
        return "count";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public T makeOperation(T a) throws CalculationException {
        return calculator.count(a);
    }
}