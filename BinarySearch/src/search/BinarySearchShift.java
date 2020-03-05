package search;

public class BinarySearchShift {
        // Pre: any i: args[i] -> Integer &&
        // exists k in (0, .. args.length] : any i, j : [0.. k) && i < j => args[i] < args[j]
        // any i, j : [k, args.length) && i < j => args[i] < args[j]
        // k < args.length => a[0] > a[k]
        // Post: k in [0.. a.size())
        public static void main(String[] args) {
        int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        //int r = iterativeBinarySearch(a);
        int r = recursiveBinarySearch(a, 0, a.length);
        // if k = a.length => any i, j in [0.. a.length) i < j => a[i] < a[j] => exists k' = 0
        System.out.println((r == a.length ? 0 : r));
    }

    // Pre: exists k in (0, .. a.length] : any i, j : [0.. k) && i < j => a[i] < a[j]
    // any i, j : [k, a.length) && i < j => a[i] < a[j]
    // k < args.length => a[0] > a[k]
    // Post: R = k
    private static int iterativeBinarySearch(int[] a) {
        int l = 0, r = a.length;
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
    }

    // Pre: exists k in (0, .. a.length] : any i, j : [0.. k) && i < j => a[i] < a[j]
    // any i, j : [k, a.length) && i < j => a[i] < a[j]
    // k < args.length => a[0] > a[k]
    // l < k && r >= k && l >= 0 && r <= a.size()
    // Post: R = k
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
            return recursiveBinarySearch(a, l, r);
        } else {
            // r - l == 1
            // l < k && r >= k && r - l == 1 => r = k
            return r;
        }
    }
}
