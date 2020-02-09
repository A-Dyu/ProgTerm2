package expression.exceptions;

public class CheckedLog2 extends AbstractUnaryOperator {
    public CheckedLog2(CommonExpression expression) {
        super(expression);
    }

    @Override
    protected int operate(int x) {
        if (x <= 0) {
            throw new LogException(x, 2);
        }
        return CheckedLog.log(x, 2);
    }

    @Override
    protected String getOperator() {
        return "log2";
    }
}

