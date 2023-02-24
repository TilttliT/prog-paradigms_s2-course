package expression.parser;

import expression.calculators.Calculator;
import expression.generic.ArithmeticExpression;

public interface BinaryConstructor<T> {
    ArithmeticExpression<T> create(Calculator<T> calculator, ArithmeticExpression<T> left,
                                   ArithmeticExpression<T> right);
}