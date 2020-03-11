package expression.parser.exceptions;

public class MultiplyOverflowException extends OperateOverflowException {
    public MultiplyOverflowException(Object a, Object b) {
        super("Multiply", a + " * " + b);
    }
}
