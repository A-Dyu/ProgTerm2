package expression.exceptions;

public class CheckedMultiply extends AbstractBinaryOperator {
    public CheckedMultiply(CommonExpression a, CommonExpression b) {
        super(a, b);
    }

    @Override
    protected String getOperator() {
        return " * ";
    }

    @Override
    protected int operate(int a, int b) {
        if (checkException(a, b)) {
            throw new MultiplyOverflowException(a, b);
        }
        return a * b;
    }

    @Override
    protected boolean isOrdered() {
        return false;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    static boolean checkException(int a, int b) {
        return (a != 0 && b != 0) && (a == Integer.MIN_VALUE && b == -1 || a == -1 && b == Integer.MIN_VALUE ||
                a * b == Integer.MIN_VALUE && Integer.MIN_VALUE / b != a ||
                a == Integer.MIN_VALUE && b != 1 || a != 1 && b == Integer.MIN_VALUE ||
                a * b != Integer.MIN_VALUE && Integer.MAX_VALUE / abs(a) < abs(b));
    }

    private static int abs(int x) {
        return x >= 0 ? x : -x;
    }
}
