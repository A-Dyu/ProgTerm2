package expression.exceptions;

abstract public class AbstractUnaryOperator implements CommonExpression {
    public AbstractUnaryOperator(CommonExpression expression) {
        this.expression = expression;
    }

    protected CommonExpression expression;

    protected abstract int operate(int x);
    protected abstract String getOperator();

    public int evaluate(int x) {
        return operate(expression.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return operate(expression.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getOperator() + "(" + expression + ")";
    }

    @Override
    public String toMiniString() {
        return getOperator() + expression.toMiniString();
    }
}
