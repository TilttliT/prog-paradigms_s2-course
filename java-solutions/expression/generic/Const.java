package expression.generic;

public class Const<T> implements ArithmeticExpression<T> {
    private final T value;

    public Const(T value) {
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
    public T evaluate(T x, T y, T z) {
        return value;
    }
}
