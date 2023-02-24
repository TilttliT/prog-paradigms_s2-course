package expression;

public class Abs extends UnaryOperation {
    public Abs(ArithmeticExpression left) {
        super(left);
    }

    @Override
    public String getOperationSymbol() {
        return "abs";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int makeOperation(int a) {
        if (a < 0) {
            return -a;
        } else {
            return a;
        }
    }
}