package expression;

public interface ArithmeticExpression extends Expression, TripleExpression {
    String toString();

    String toMiniString();

    int getPriority();

    int evaluate(int x);

    int evaluate(int x, int y, int z);
}