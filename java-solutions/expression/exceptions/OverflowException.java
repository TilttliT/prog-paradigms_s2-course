package expression.exceptions;

public final class OverflowException extends CalculationException {
    public OverflowException(String operator, int a, int b) {
        super(String.format("Get overflow, when try: %d %s %d", a, operator, b));
    }

    public OverflowException(String operator, int a) {
        super(String.format("Get overflow, when try: %s %d", operator, a));
    }
}