package search;

public class BinarySearchShift {
        // Pre: any i: args[i] -> Integer &&
        // exists k in [0, .. args.length] : any i, j : [0.. k) && i < j => args[i] < args[j]
        // any i, j : [k, args.length) && i < j => args[i] < args[j]
        // min(args[i]: i in [0.. k)) > max(args[j]: j in [k, args.length)
        public static void main(String[] args) {
        int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        int r = iterativeBinarySearch(a);
        //int r = recursiveBinarySearch(a, -1, a.size());
        // if k = a.length => any i, j in [0.. a.length) i < j => a[i] < a[j] => exists k' = 0
        System.out.println((r == a.length ? 0 : r));
    }   // Post: k in [0.. a.size())

    // Pre: exists k in [0, .. a.length] : any i, j : [0.. k) && i < j => a[i] < a[j]
    // any i, j : [k, a.length) && i < j => a[i] < a[j]
    // min(a[i]: i in [0.. k)) > max(a[j]: j in [k, a.length)
    private static int iterativeBinarySearch(int[] a) {
        int l = -1, r = a.length;
        // Inv: l < k && r >= k
        while (r - l > 1) {
            // r - l > 1
            int m = (l + r) / 2;
            // l < m && m < r
            if (a[m] < a[0]) {
                // a[m] < a[0] => m >= k
                r = m;
                // a[r'] < k
                // r' - l < r - l
            } else {
                // a[m] > a[0] => m < k
                l = m;
                // a[l'] < k
                // r - l' < r - l
            }
            // r' - l' < r - l
            // r > l
        }
        // (r - l = 1)
        // l < k && r >= k && r - l == 1 => r = k
        return r;
    }  // Post: r = k && 0 <= k <= a.size()

    // Pre: exists k in [0, .. a.length] : any i, j : [0.. k) && i < j => a[i] < a[j]
    // any i, j : [k, a.length) && i < j => a[i] < a[j]
    // min(a[i]: i in [0.. k)) > max(a[j]: j in [k, a.length)
    // Inv: l < k && r >= k
    private static int recursiveBinarySearch(int[] a, int l, int r) {
        if (r - l > 1) {
            // r - l > 1
            int m = (l + r) / 2;
            // l < m && m < r
            if (a[m] < a[0]) {
                // a[m] < a[0] => m >= k
                r = m;
                // r' >= k
                // r' - l < r - l
            } else {
                // a[m] > a[0] => m < k
                l = m;
                // l' < k
                // r - l' < r - l
            }
            // r' - l' < r - l
            // r > l
            return r = recursiveBinarySearch(a, l, r);
        } else {
            // r - l == 1
            // l < k && r >= k && r - l == 1 => r = k
            return r;
        }
    }   // Post: r = k && 0 <= k <= a.size()
}
