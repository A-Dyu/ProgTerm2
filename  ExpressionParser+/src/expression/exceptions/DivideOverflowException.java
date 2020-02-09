package expression.exceptions;

public class DivideOverflowException extends OperateOverflowException {
    public DivideOverflowException() {
        super("Divide", Integer.MIN_VALUE + " / -1");
    }
}
