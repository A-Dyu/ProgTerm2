package expression.parser;

import expression.parser.exceptions.ParserException;

public interface Source {
    ParserException error(String message);
    boolean hasNext();
    char next();
}
