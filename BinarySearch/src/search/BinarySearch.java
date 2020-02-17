package search;
import java.util.ArrayList;
import java.util.List;

public class BinarySearch {
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        List<Integer> a = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            a.add(Integer.parseInt(args[i]));
        }
        // Pre: i < j => a[i] >= a[j]
        int l = -1, r = a.size();
        // Inv: (i <= l && 0 <= i < a.size()) => a[i] > x && (i >= r && 0 <= i < a.size()) => a[i] <= x
        while (r - l > 1) {
            // r - l > 1
            int m = (l + r) / 2;
            // m > l && r < m
            if (a.get(m) <= x) {
                // i >= m => a[i] <= x
                r = m;
                // i >= r' => a[i] <= x
                // r' - l < r - l
            } else {
                // i <= m => a[i] > x
                l = m;
                // i <= l' => a[i] > x
                // r - l' < r - l
            }
            // r' - l' < r - l
            // r > l
        }
        // Post: ((r > l && r - l <= 1 => r - l = 1) || (l = r = 0)) => r = min(i : i >= 0 && a[i] <= x): a[i] <= x
        System.out.println(r);
    }
}
