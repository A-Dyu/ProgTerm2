package expression.parser.exceptions;

public class DivideByZeroException extends ExpressionException {
    public <T extends Number>DivideByZeroException(T a) {
        super("Divide by zero: " + a + " / 0");
    }
}
