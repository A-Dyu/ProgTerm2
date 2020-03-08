package expression.parser.exceptions;

public class AddOverflowException extends OperateOverflowException {
    public <T>AddOverflowException(T a, T b) {
        super("Add", a + " + " + b);
    }
}
