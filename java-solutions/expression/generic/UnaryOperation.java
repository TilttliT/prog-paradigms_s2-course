package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public abstract class UnaryOperation<T> implements ArithmeticExpression<T> {
    private final ArithmeticExpression<T> left;
    protected final Calculator<T> calculator;

    public abstract String getOperationSymbol();

    public abstract int getPriority();

    protected abstract T makeOperation(T a) throws CalculationException;

    public UnaryOperation(final Calculator<T> calculator, final ArithmeticExpression<T> left) {
        this.left = left;
        this.calculator = calculator;
    }

    private String toBrackets(String s) {
        return "(" + s + ")";
    }

    private String operand() {
        int leftPriority = left.getPriority();
        int priority = getPriority();

        if (leftPriority > priority) {
            return toBrackets(left.toMiniString());
        } else {
            return " " + left.toMiniString();
        }
    }

    @Override
    public String toMiniString() {
        return getOperationSymbol() + operand();
    }

    @Override
    public String toString() {
        return getOperationSymbol() + toBrackets(left.toString());
    }

    @Override
    public T evaluate(T x, T y, T z) throws CalculationException {
        return makeOperation(left.evaluate(x, y, z));
    }
}