package expression.parser.exceptions;

public class DivideOverflowException extends OperateOverflowException {
    public DivideOverflowException(Number a, Number b) {
        super("Divide", a + " / " + b);
    }
}
