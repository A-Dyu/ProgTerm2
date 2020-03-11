package expression.parser.exceptions;

public class DivideOverflowException extends OperateOverflowException {
    public DivideOverflowException(Object a, Object b) {
        super("Divide", a + " / " + b);
    }
}
