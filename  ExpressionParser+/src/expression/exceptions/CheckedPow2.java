package expression.exceptions;

public class CheckedPow2 extends AbstractUnaryOperator {
    public CheckedPow2(CommonExpression expression) {
        super(expression);
    }

    @Override
    protected int operate(int x) {
        if (checkException(x)) {
            throw new PowOverflowException(2, x);
        }
        return (1 << x);
    }

    @Override
    protected String getOperator() {
        return "pow2";
    }

    static boolean checkException(int x) {
        return x < 0 || x > 31;
    }
}
