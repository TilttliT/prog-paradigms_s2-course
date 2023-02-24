package expression.calculators;

import expression.exceptions.ParserException;

public interface Calculator<T> {
    T add(T a, T b);

    T multiply(T a, T b);

    T divide(T a, T b);

    T subtract(T a, T b);

    T parseConst(String constString) throws ParserException;

    T parseConst(int constInt);

    T min(T a, T b);

    T max(T a, T b);

    T count(T a);

    T negate(T a);
}