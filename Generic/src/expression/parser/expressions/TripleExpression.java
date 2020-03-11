package expression.parser.expressions;

public interface TripleExpression<T> {
    T evaluate(int x, int y, int z);
}