package expression.parser.expressions;

import expression.parser.operator.Operator;

public class Min<T> extends AbstractBinaryOperator<T> {
    public Min(GenericExpression<T> a, GenericExpression<T> b, Operator<T> operator) {
        super(a, b, operator);
    }

    @Override
    protected String getOperator() {
        return " min ";
    }

    @Override
    protected int getPriority() {
        return 0;
    }

    @Override
    protected T operate(T a, T b) {
        return operator.min(a, b);
    }

    @Override
    protected boolean isOrdered() {
        return false;
    }
}
