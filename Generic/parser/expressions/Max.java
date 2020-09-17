package expression.parser.expressions;

import expression.parser.operator.Operator;

public class Max<T> extends AbstractBinaryOperator<T> {
    public Max(GenericExpression<T> a, GenericExpression<T> b, Operator<T> operator) {
        super(a, b, operator);
    }

    @Override
    protected String getOperator() {
        return " max ";
    }

    @Override
    protected int getPriority() {
        return 0;
    }

    @Override
    protected T operate(T a, T b) {
        return operator.max(a, b);
    }

    @Override
    protected boolean isOrdered() {
        return false;
    }
}
