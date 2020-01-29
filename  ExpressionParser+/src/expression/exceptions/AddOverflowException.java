package expression.exceptions;

public class AddOverflowException extends OperateOverflowException {
    public AddOverflowException(int a, int b) {
        super("Add", a + " + " + b);
    }
}
