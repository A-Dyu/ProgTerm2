package expression.parser.exceptions;

public class AddOverflowException extends OperateOverflowException {
    public AddOverflowException(Number a, Number b) {
        super("Add", a + " + " + b);
    }
}
