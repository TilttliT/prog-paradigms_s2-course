package expression;

public class Const implements ArithmeticExpression {
    private final Number value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return value.toString();
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const constValue) {
            return value.equals(constValue.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}