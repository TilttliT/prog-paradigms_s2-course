package expression.generic;

public class Variable<T> implements ArithmeticExpression<T> {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            default  -> z;
        };
    }

    @Override
    public String toString() {
        return name;
    }
}