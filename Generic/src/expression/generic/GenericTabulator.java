package expression.generic;

import expression.expressions.CommonExpression;
import expression.parser.exceptions.ExpressionException;
import expression.parser.ExpressionParser;

public class GenericTabulator<T extends Number> implements Tabulator {

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        ExpressionParser parser = new ExpressionParser<>();
        CommonExpression commonExpression = parser.parse(expression, mode);
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int x = 0; x <= x2 - x1; x++) {
            for (int y = 0; y <= y2 - y1; y++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    try {
                        ans[x][y][z] = commonExpression.evaluate(x + x1, y + y1, z + z1);
                    } catch (ExpressionException e) {
                        ans[x][y][z] = null;
                    }
                }
            }
        }
        return ans;
    }
}
