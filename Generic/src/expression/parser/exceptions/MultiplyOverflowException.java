package expression.parser.exceptions;

public class MultiplyOverflowException extends OperateOverflowException {
    public MultiplyOverflowException(Number a, Number b) {
        super("Multiply", a + " * " + b);
    }
}
