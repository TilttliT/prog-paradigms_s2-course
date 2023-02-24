package expression;

import java.util.Objects;

public abstract class BinaryOperation implements ArithmeticExpression {
    public final ArithmeticExpression left;
    public final ArithmeticExpression right;

    public abstract String getOperationSymbol();

    public abstract int getPriority();

    public abstract int makeOperation(int a, int b);

    public BinaryOperation(final ArithmeticExpression left, final ArithmeticExpression right) {
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
        if (right instanceof Divide) {
            ++rightPriority;
        }

        if (rightPriority > priority || rightPriority == priority
                && (right.getClass() == this.getClass() && right instanceof NotAssociativityOperations
                || right.getClass() != this.getClass() && this instanceof NotAssociativityOperations)) {

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
    public int hashCode() {
        return Objects.hash(left, right, getOperationSymbol());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation binary) {
            return binary.getClass() == this.getClass() &&
                    this.left.equals(binary.left) &&
                    this.right.equals(binary.right);
        }
        return false;
    }

    @Override
    public String toString() {
        return toBrackets(left.toString() + operator() + right.toString());
    }

    @Override
    public int evaluate(int x) {
        return makeOperation(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}