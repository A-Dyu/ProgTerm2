package expression.generic;

import expression.parser.expressions.CommonExpression;
import expression.parser.exceptions.ExpressionException;
import expression.parser.ExpressionParser;

import expression.parser.operator.*;

import java.util.Map;

public class GenericTabulator <T> implements Tabulator {
    private static final Map<String, Operator> MODES = Map.of(
                "i", new IntegerOperator(true),
                "d", new DoubleOperator(),
                "bi", new BigIntegerOperator(),
                "u", new IntegerOperator(false),
                "f", new FloatOperator(),
                "b", new ByteOperator()
            );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        @SuppressWarnings("unchecked")
        Operator<T> operator = MODES.get(mode);
        ExpressionParser<T> parser = new ExpressionParser<>(operator);
        CommonExpression<T> commonExpression = parser.parse(expression);
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = 0; x <= x2 - x1; x++) {
            for (int y = 0; y <= y2 - y1; y++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    try {
                        ans[x][y][z] = commonExpression.evaluate(operator.parse(x + x1), operator.parse(y + y1), operator.parse(z + z1));
                    } catch (ExpressionException ignored) {}
                }
            }
        }
        return ans;
    }
}
