package expression.parser.expressions;

import expression.parser.operator.Operator;

public class Count<T> extends AbstractUnaryOperator<T> {
    public Count(GenericExpression<T> expression, Operator<T> operator) {
        super(expression, operator);
    }

    @Override
    protected T operate(T x) {
        return operator.count(x);
    }

    @Override
    protected String getOperator() {
        return "count";
    }
}
