package expression.exceptions;

public class CheckedPow extends AbstractBinaryOperator {
    public CheckedPow(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected String getOperator() {
        return "**";
    }

    @Override
    protected int getPriority() {
        return 3;
    }

    @Override
    protected int operate(int a, int b) {
        if (a == 0 && b == 0 || b < 0) {
            throw new PowException(a, b);
        }
        return binPow(a, b);
    }

    private static int binPow(int a, int b) {
        int n = b, ans = 1, x = a;
        while (n > 0) {
            if (n % 2 == 1) {
                if (CheckedMultiply.checkException(ans, x)) {
                    throw new PowOverflowException(a, b);
                }
                ans *= x;
            }
            if (n != 1 && CheckedMultiply.checkException(x, x)) {
                throw new PowOverflowException(a, b);
            }
            x *= x;
            n = (n >> 1);
        }
        return ans;
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }
}
