package expression;

public class LeftShift extends BinaryOperation implements NotAssociativityOperations {
    public LeftShift(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "<<";
    }

    @Override
    public int getPriority() {
        return 30;
    }

    @Override
    public int makeOperation(int a, int b) {
        return a << b;
    }
}