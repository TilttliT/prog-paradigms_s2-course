package expression.exceptions;

public final class IllegalPriorityException extends ParserException {
    public IllegalPriorityException(int priority) {
        super(String.format("Priority %d missing in priority list", priority));
    }
}