package expression.parser.exceptions;

public class SubtractOverflowException extends OperateOverflowException {
    public SubtractOverflowException(Object a, Object b) {
        super("Subtract", a + " - " + b);
    }
}
