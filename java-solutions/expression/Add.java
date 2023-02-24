package expression;

public class Add extends BinaryOperation {
    public Add(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public int makeOperation(int a, int b) {
        return a + b;
    }
}