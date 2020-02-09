package expression.exceptions;

public class CheckedAdd extends AbstractBinaryOperator {
    public CheckedAdd(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected String getOperator() {
        return " + ";
    }

    @Override
    protected int operate(int a, int b) {
        if (checkException(a, b)) {
            throw new AddOverflowException(a, b);
        }
        return a + b;
    }

    @Override
    protected boolean isOrdered() {
        return false;
    }

    static boolean checkException(int a, int b) {
        return b > 0 && Integer.MAX_VALUE - b < a || b < 0 && Integer.MIN_VALUE - b > a;
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
