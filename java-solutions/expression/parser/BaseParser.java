package expression.parser;

import expression.exceptions.IllegalSymbolException;
import expression.exceptions.ParserException;

public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void expect(final char expected) throws ParserException {
        if (!take(expected)) {
            throw new IllegalSymbolException(String.valueOf(ch), getPos(), String.valueOf(expected));
        }
    }

    protected void expect(final String value) throws ParserException {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected int skipWhitespace() {
        int cnt = 0;
        while (!eof() && Character.isWhitespace(ch)) {
            // skip
            take();
            ++cnt;
        }
        return cnt;
    }

    protected int getPos() {
        return source.getPos();
    }

    public char getCurrentChar() {
        return ch;
    }

    protected void expectBracketsAndSkipWhitespace() throws ParserException {
        if (!test('(') && skipWhitespace() == 0) {
            throw new IllegalSymbolException(String.valueOf(ch), getPos(), "'(' or ' '");
        }
    }

    protected void back() {
        if (source.getPos() > 1) {
            source.back();
            source.back();
            take();
        }
    }
}