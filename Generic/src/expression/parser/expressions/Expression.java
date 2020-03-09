package expression.parser.expressions;

public interface Expression<T extends Number> extends ToMiniString {
    T evaluate(int x);
}
