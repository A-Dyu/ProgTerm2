package expression.exceptions;

public class CheckedNegate extends AbstractUnaryOperator {
    public CheckedNegate(CommonExpression expression) {
        super(expression);
    }

    @Override
    protected int operate(int x) {
        if (checkException(x)) {
            throw new NegateOverflowException(x);
        }
        return -x;
    }

    @Override
    protected String getOperator() {
        return "-";
    }


    static boolean checkException(int x) {
        return x == Integer.MIN_VALUE;
    }

    @Override
    public int evaluate(int x) {
        return operate(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return operate(expression.evaluate(x, y, z));
    }
}
