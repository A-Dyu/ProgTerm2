package expression.parser.exceptions;

public class DivideOverflowException extends OperateOverflowException {
    public <T extends Number>DivideOverflowException(T a, T b) {
        super("Divide", a + " / " + b);
    }
}
