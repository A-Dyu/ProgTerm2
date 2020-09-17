package expression.parser.expressions;

import expression.exceptions.ExpressionException;
import java.util.Objects;
import expression.parser.operator.*;

public class Variable<T> implements GenericExpression<T> {
    private String var;
    private final Operator<T> operator;

    public Variable(String var, final Operator<T> operator) {
        this.var = var;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return var;
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
    public T evaluate(T x, T y, T z) {
        if (var.equals("x")) {
            return x;
        }
        if (var.equals("y")) {
            return y;
        }
        if (var.equals("z")) {
            return z;
        }
        throw new ExpressionException("Unexpected variable " + var);
    }
}
