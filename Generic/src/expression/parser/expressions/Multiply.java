package expression.parser.expressions;

import expression.parser.operator.*;

public class Multiply<T> extends AbstractBinaryOperator<T> {
    public Multiply(GenericExpression<T> a, GenericExpression<T> b, final Operator<T> operator) {
        super(a, b, operator);
    }

    @Override
    protected String getOperator() {
        return " * ";
    }

    @Override
    protected boolean isOrdered() {
        return false;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    protected T operate(T a, T b) {
        return operator.multiply(a, b);
    }
}
