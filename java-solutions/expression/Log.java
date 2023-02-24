package expression;

public class Log extends BinaryOperation implements NotAssociativityOperations {
    public Log(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "//";
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public int makeOperation(int a, int b) {
        int res = 0;
        while (a >= b) {
            ++res;
            a /= b;
        }
        return res;
    }
}