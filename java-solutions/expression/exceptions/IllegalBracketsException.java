package expression.exceptions;

public final class IllegalBracketsException extends ParserException {
    public IllegalBracketsException(int pos) {
        super(String.format("Found an extra or missing bracket in position %d", pos));
    }
}