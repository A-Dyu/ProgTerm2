package expression.parser.exceptions;

public class NegateOverflowException extends OperateOverflowException {
    public NegateOverflowException(Number x) {
        super("Negate", "-" + x);
    }
}
