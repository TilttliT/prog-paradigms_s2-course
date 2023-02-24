package expression;

import java.util.Objects;

public abstract class UnaryOperation implements ArithmeticExpression {
    public final ArithmeticExpression left;

    public abstract String getOperationSymbol();
    public abstract int getPriority ();
    public abstract int makeOperation (int a);

    public UnaryOperation(final ArithmeticExpression left) {
        this.left = left;
    }

    private String toBrackets (String s) {
        return "(" + s + ")";
    }

    private String operand () {
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
    public int hashCode() {
        return Objects.hash(left, getOperationSymbol());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation binary) {
            return binary.getClass() == this.getClass() &&
                    this.left.equals(binary.left);
        }
        return false;
    }

    @Override
    public String toString() {
        return getOperationSymbol() + toBrackets(left.toString());
    }

    @Override
    public int evaluate (int x) {
        return makeOperation(left.evaluate(x));
    }

    @Override
    public int evaluate (int x, int y, int z) {
        return makeOperation(left.evaluate(x, y, z));
    }
}
