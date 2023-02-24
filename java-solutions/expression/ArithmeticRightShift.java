package expression;

public class ArithmeticRightShift extends BinaryOperation implements NotAssociativityOperations {
    public ArithmeticRightShift(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationSymbol() {
        return ">>>";
    }

    @Override
    public int getPriority() {
        return 30;
    }

    @Override
    public int makeOperation(int a, int b) {
        return a >>> b;
    }
}