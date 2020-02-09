package expression.exceptions;

public class PowOverflowException extends OperateOverflowException {
    public PowOverflowException(int a, int b) {
        super("Pow", a + "^" + b);
    }
}
