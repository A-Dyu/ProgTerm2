package expression.exceptions;

public interface Source {
    ParserException error(String message);
    boolean hasNext();
    char next();
}
