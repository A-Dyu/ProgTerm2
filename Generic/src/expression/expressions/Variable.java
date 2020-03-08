package expression.expressions;

import expression.parser.exceptions.ExpressionException;
import java.util.Objects;
import expression.parser.operator.*;

public class Variable<T extends Number> implements CommonExpression<T> {
    private String var;
    private final Operator<T> operator;

    public Variable(String var, final Operator<T> operator) {
        this.var = var;
        this.operator = operator;
    }

    @Override
    public T evaluate(int x) {
        return operator.parse(x);
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) { return false; }
        Variable variable = (Variable) object;
        return Objects.equals(var, variable.var);
    }

    @Override
    public int hashCode() {
        return Objects.hash(var);
    }

    @Override
    public T evaluate(int x, int y, int z) {
        if (var.equals("x")) {
            return operator.parse(x);
        }
        if (var.equals("y")) {
            return operator.parse(y);
        }
        if (var.equals("z")) {
            return operator.parse(z);
        }
        throw new ExpressionException("Unexpected variable " + var);
    }
}
