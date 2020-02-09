package expression.exceptions;

public class CheckedDivide extends AbstractBinaryOperator {
    public CheckedDivide(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected String getOperator() {
        return " / ";
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected int operate(int a, int b) {
        if (b == 0) {
            throw new DivideByZeroException(a);
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new DivideOverflowException();
        }
        return a / b;
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }

}
