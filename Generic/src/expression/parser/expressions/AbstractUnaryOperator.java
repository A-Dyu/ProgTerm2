package expression.parser.expressions;

import expression.parser.operator.*;

abstract public class AbstractUnaryOperator<T> implements GenericExpression<T> {
    protected final Operator<T> operator;
    public AbstractUnaryOperator(GenericExpression<T> expression, final Operator<T> operator) {
        this.expression = expression;
        this.operator = operator;
    }

    protected GenericExpression<T> expression;

    protected abstract T operate(T x);
    protected abstract String getOperator();

    @Override
    public T evaluate(T x, T y, T z) {
        return operate(expression.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getOperator() + "(" + expression + ")";
    }
}
