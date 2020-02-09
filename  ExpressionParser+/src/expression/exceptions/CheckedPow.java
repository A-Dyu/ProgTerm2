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
        try {
            return binPow(a, b);
        } catch (IllegalArgumentException e) {
            throw new PowOverflowException(a, b);
        }
    }

    private static int binPow(int a, int b) {
        if (b == 0) {
            return 1;
        }
        int ans = binPow(a, b / 2);
        if (CheckedMultiply.checkException(ans, ans)) {
            throw new IllegalArgumentException();
        }
        ans *= ans;
        if (b % 2 == 1) {
            if (CheckedMultiply.checkException(ans, a)) {
                System.out.println(ans + " " + a);
                throw new IllegalArgumentException();
            }
            ans *= a;
        }
        return ans;
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }
}
