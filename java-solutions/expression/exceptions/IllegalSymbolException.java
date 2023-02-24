package expression.exceptions;

public final class IllegalSymbolException extends ParserException {
    public IllegalSymbolException(String get, int pos, String expect) {
        super(String.format("Get %s in position %d, when expected: %s", get, pos, expect));
    }
}