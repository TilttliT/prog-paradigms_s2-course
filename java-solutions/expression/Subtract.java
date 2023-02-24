package expression;

public class Subtract extends BinaryOperation implements NotAssociativityOperations {
    public Subtract(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "-";
    }

    @Override
    public int getPriority () {
        return 20;
    }

    @Override
    public int makeOperation(int a, int b) {
        return a - b;
    }
}
