package expression.parser.exceptions;

public class DivideByZeroException extends ExpressionException {
    public DivideByZeroException(Object a) {
        super("Divide by zero: " + a + " / 0");
    }
}
