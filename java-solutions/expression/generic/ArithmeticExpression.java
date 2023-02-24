package expression.generic;

import expression.exceptions.CalculationException;

public interface ArithmeticExpression<T> {
    String toString();

    String toMiniString();

    int getPriority();

    T evaluate(T x, T y, T z) throws CalculationException;
}