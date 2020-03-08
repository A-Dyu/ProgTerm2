package expression.expressions;

import expression.parser.operator.*;

public class Add<T extends Number> extends AbstractBinaryOperator<T> {
    public Add(CommonExpression<T> a, CommonExpression<T> b, final Operator<T> operator) {
        super(a, b, operator);
    }

    @Override
    protected String getOperator() {
        return " + ";
    }

    @Override
    protected boolean isOrdered() {
        return false;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    protected T operate(T a, T b) {
        return operator.add(a, b);
    }
}
