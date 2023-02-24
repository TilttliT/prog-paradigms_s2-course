package expression.exceptions;

public final class IllegalLogArgumentsException extends CalculationException {
    public IllegalLogArgumentsException(int a, int b) {
        super(String.format("Found illegal log arguments value: %d %d", a, b));
    }
}