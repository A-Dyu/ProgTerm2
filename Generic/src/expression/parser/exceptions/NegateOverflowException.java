package expression.parser.exceptions;

public class NegateOverflowException extends OperateOverflowException {
    public <T extends Number>NegateOverflowException(T x) {
        super("Negate", "-" + x);
    }
}
