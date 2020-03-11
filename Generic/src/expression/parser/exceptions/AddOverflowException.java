package expression.parser.exceptions;

public class AddOverflowException extends OperateOverflowException {
    public AddOverflowException(Object a, Object b) {
        super("Add", a + " + " + b);
    }
}
