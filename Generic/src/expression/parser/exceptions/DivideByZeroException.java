package expression.parser.exceptions;

public class DivideByZeroException extends ExpressionException {
    public DivideByZeroException(Number a) {
        super("Divide by zero: " + a + " / 0");
    }
}
