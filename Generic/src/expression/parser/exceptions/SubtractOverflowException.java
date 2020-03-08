package expression.parser.exceptions;

public class SubtractOverflowException extends OperateOverflowException {
    public <T extends Number>SubtractOverflowException(T a, T b) {
        super("Subtract", a + " - " + b);
    }
}
