package expression.exceptions;

public class DivideByZeroException extends ExpressionException {
    public DivideByZeroException(int a) {
        super("Divide by zero: " + a + " / 0");
    }
}
