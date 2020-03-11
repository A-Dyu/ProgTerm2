package expression.parser.exceptions;

public class NegateOverflowException extends OperateOverflowException {
    public NegateOverflowException(Object x) {
        super("Negate", "-" + x);
    }
}
