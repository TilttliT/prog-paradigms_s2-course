package expression.exceptions;

public final class DBZException extends CalculationException {
    public DBZException(String a, String b) {
        super(String.format("Found division by zero: %s / %s", a, b));
    }
}