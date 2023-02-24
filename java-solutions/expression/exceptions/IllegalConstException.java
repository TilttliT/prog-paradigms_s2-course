package expression.exceptions;

public final class IllegalConstException extends ParserException {
    public IllegalConstException(int pos, String number) {
        super(String.format("Found illegal const value in position %d: %s", pos, number));
    }

    public IllegalConstException(String message) {
        super(message);
    }
}