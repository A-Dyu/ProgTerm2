package expression.expressions;

import expression.parser.operator.*;

abstract public class AbstractUnaryOperator<T extends Number> implements CommonExpression<T> {
    protected final Operator<T> operator;
    public AbstractUnaryOperator(CommonExpression<T> expression, final Operator<T> operator) {
        this.expression = expression;
        this.operator = operator;
    }

    protected CommonExpression<T> expression;

    protected abstract T operate(T x);
    protected abstract String getOperator();

    @Override
    public T evaluate(int x) {
        return operate(expression.evaluate(x));
    }

    @Override
    public T evaluate(int x, int y, int z) {
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
