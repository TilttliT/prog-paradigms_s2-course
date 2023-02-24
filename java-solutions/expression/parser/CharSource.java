package expression.parser;

public interface CharSource {
    boolean hasNext();

    char next();

    void back();

    int getPos();
}