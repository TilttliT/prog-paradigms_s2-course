package expression.exceptions;

public final class IllegalPowArgumentsException extends CalculationException {
    public IllegalPowArgumentsException(int a, int b) {
        super(String.format("Found illegal pow arguments value: %d %d", a, b));
    }
}