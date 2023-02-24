package expression;

public class Negate extends UnaryOperation {
    public Negate(ArithmeticExpression left) {
        super(left);
    }

    @Override
    public String getOperationSymbol() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int makeOperation(int a) {
        return -a;
    }
}