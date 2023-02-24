package expression.exceptions;

public abstract class CalculationException extends RuntimeException {
    public CalculationException(String message) {
        super(message);
    }
}