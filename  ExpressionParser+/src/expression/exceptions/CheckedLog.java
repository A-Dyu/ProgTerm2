package expression.exceptions;

public class CheckedLog extends AbstractBinaryOperator {
    public CheckedLog(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected String getOperator() {
        return "//";
    }

    @Override
    protected int getPriority() {
        return 3;
    }

    @Override
    protected int operate(int a, int b) {
        if (a <= 0 || b <= 0 || b == 1) {
            throw new LogException(a, b);
        }
        return log(a, b);
    }

    static int log(int a, int b) {
        int ans = 0;
        while (a >= b) {
            ans++;
            a /= b;
        }
        return ans;
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }
}
