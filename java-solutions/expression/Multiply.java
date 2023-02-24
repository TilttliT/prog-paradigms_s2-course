package expression;

public class Multiply extends BinaryOperation {
    public Multiply(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
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
    public int makeOperation(int a, int b) {
        return a * b;
    }
}