package expression.expressions;

import java.util.Objects;

import expression.parser.operator.*;

public abstract class AbstractBinaryOperator<T extends Number> implements CommonExpression<T> {
    private CommonExpression<T> a;
    private CommonExpression<T> b;
    protected final Operator<T> operator;

    public AbstractBinaryOperator(CommonExpression<T> a, CommonExpression<T> b, final Operator<T> operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
    }


    protected abstract String getOperator();
    protected abstract int getPriority();
    protected abstract T operate(T a, T b);
    protected abstract boolean isOrdered();

    @Override
    public T evaluate(int x) {
        return operate(a.evaluate(x), b.evaluate(x));
    }

    @Override
    public T evaluate(int x, int y, int z) { return operate(a.evaluate(x, y, z), b.evaluate(x, y, z));}

    @Override
    public String toString() {
        return "(" + a + getOperator()+ b + ')';
    }

    private String getExpression(Expression a, boolean isInBrackets) {
        return (isInBrackets ? "(" : "") + a.toMiniString() + (isInBrackets ? ")" : "");
    }

    private boolean checkPriorityBrackets(Expression a) {
        return ((a instanceof AbstractBinaryOperator) && ((AbstractBinaryOperator) a).getPriority() < this.getPriority());
    }

    private boolean checkOrderAdditionBrackets(Expression a) {
        if (a instanceof AbstractBinaryOperator) {
            AbstractBinaryOperator binA = (AbstractBinaryOperator) a;
            if (binA.isOrdered() && binA.getPriority() <= this.getPriority()) {
                return true;
            }
            return (this.isOrdered()) && (binA.getPriority() <= this.getPriority());
        }
        return false;
    }

    @Override
    public String toMiniString() {
        return getExpression(a, checkPriorityBrackets(a)) +
                getOperator() +
                getExpression(b, checkPriorityBrackets(b) || checkOrderAdditionBrackets(b));
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AbstractBinaryOperator abstractBinaryOperator = (AbstractBinaryOperator) object;
        return this.a.equals(abstractBinaryOperator.a) && b.equals(abstractBinaryOperator.b);
    }

    @Override
    public int hashCode() {
        return 42424241 * a.hashCode() + 31 * 31 * (Objects.hash(getOperator())) + 31 * b.hashCode();
    }
}
