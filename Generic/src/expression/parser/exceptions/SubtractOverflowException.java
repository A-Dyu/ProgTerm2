package expression.parser.exceptions;

public class SubtractOverflowException extends OperateOverflowException {
    public SubtractOverflowException(Number a, Number b) {
        super("Subtract", a + " - " + b);
    }
}
