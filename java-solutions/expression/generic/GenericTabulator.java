package expression.generic;

import expression.calculators.*;
import expression.exceptions.CalculationException;
import expression.exceptions.ParserException;
import expression.parser.ExpressionParser;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    static final Map<String, Calculator<?>> mapOfModes;

    static {
        mapOfModes = Map.of(
                "i", new IntegerCalculator(true),
                "d", new DoubleCalculator(),
                "bi", new BigIntegerCalculator(),
                "u", new IntegerCalculator(false),
                "l", new LongCalculator(),
                "t", new TruncIntegerCalculator()
        );
    }

    @Override
    public Object[][][] tabulate(String mode, String expression,
                                 int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return calculate(expression, mapOfModes.get(mode), x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] calculate(String expressionString, Calculator<T> calculator,
                                       int x1, int x2, int y1, int y2, int z1, int z2) throws ParserException {
        ExpressionParser<T> parser = new ExpressionParser<>();
        ArithmeticExpression<T> expression;
        expression = parser.parse(calculator, expressionString);

        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    try {
                        result[x - x1][y - y1][z - z1] = expression.evaluate(
                                calculator.parseConst(x),
                                calculator.parseConst(y),
                                calculator.parseConst(z)
                        );
                    } catch (CalculationException e) {
                        result[x - x1][y - y1][z - z1] = null;
                    }
                }
            }
        }

        return result;
    }
}