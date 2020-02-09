package expression.exceptions;

public class LogException extends ExpressionException {
    public LogException(int a, int b) {
        super("Invalid log arguments: " + a + " " + b);
    }
}
