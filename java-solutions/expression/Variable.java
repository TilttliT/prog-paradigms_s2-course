package expression;

public class Variable implements ArithmeticExpression {
    final String name;

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
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            default -> z;
        };
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable variable) {
            return name.equals(variable.name);
        }
        return false;
    }
}