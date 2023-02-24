package expression;

public class Divide extends BinaryOperation implements NotAssociativityOperations {
    public Divide(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
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
    public int makeOperation(int a, int b) {
        return a / b;
    }
}