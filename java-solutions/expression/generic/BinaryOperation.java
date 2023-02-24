package expression.generic;

import expression.calculators.Calculator;
import expression.exceptions.CalculationException;

public abstract class BinaryOperation<T> implements ArithmeticExpression<T> {
    private final ArithmeticExpression<T> left;
    private final ArithmeticExpression<T> right;
    protected final Calculator<T> calculator;

    public abstract String getOperationSymbol();

    public abstract int getPriority();

    protected abstract T makeOperation(T a, T b) throws CalculationException;

    public BinaryOperation(final Calculator<T> calculator, final ArithmeticExpression<T> left,
                           final ArithmeticExpression<T> right) {
        this.calculator = calculator;
        this.left = left;
        this.right = right;
    }

    private String toBrackets(String s) {
        return "(" + s + ")";
    }

    private String operator() {
        return " " + getOperationSymbol() + " ";
    }

    private String leftOperator() {
        int priority = getPriority();
        int leftPriority = left.getPriority();
        if (leftPriority > priority) {
            return toBrackets(left.toMiniString());
        } else {
            return left.toMiniString();
        }
    }

    private String rightOperator() {
        int priority = getPriority();
        int rightPriority = right.getPriority();
        if (right instanceof Divide<?>) {
            ++rightPriority;
        }

        if (rightPriority > priority || rightPriority == priority
                && (right.getClass() == this.getClass() && right instanceof NotAssociativityOperations<?>
                || right.getClass() != this.getClass() && this instanceof NotAssociativityOperations<?>)) {

            return toBrackets(right.toMiniString());
        } else {
            return right.toMiniString();
        }
    }

    @Override
    public String toMiniString() {
        return leftOperator() + operator() + rightOperator();
    }

    @Override
    public String toString() {
        return toBrackets(left.toString() + operator() + right.toString());
    }

    @Override
    public T evaluate(T x, T y, T z) throws CalculationException {
        return makeOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}