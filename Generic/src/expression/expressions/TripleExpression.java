package expression.expressions;

public interface TripleExpression<T extends Number> extends ToMiniString {
    T evaluate(int x, int y, int z);
}