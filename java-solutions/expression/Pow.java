package expression;

public class Pow extends BinaryOperation implements NotAssociativityOperations {
    public Pow(ArithmeticExpression left, ArithmeticExpression right) {
        super(left, right);
    }

    @Override
    public String getOperationSymbol() {
        return "**";
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public int makeOperation(int a, int b) {
        int res = 1;
        while (b > 0) {
            if (b % 2 == 1) {
                res *= a;
            }
            a *= a;
            b /= 2;
        }
        return res;
    }
}
