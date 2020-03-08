package expression.parser.exceptions;

public class MultiplyOverflowException extends OperateOverflowException {
    public <T extends Number>MultiplyOverflowException(T a, T b) {
        super("Multiply", a + " * " + b);
    }
}
