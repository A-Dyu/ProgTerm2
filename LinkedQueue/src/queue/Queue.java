package queue;

import java.util.function.Predicate;

// Inv: a[0].. a[n - 1]
// n >= 0
// any i: 0 <= i < n => a[i] != null
public interface Queue {
    // Pre: element != null
    // Post: n' = n + 1
    // a[n]' = element && any i: 0 <= i < n => a[i]' = a[i]
    void enqueue(Object element);

    // Pre: n > 0
    // Post: n' = n - 1
    // any i: 0 <= i < n' => a[i]' = a[i + 1]
    // R = a[0]
    Object dequeue();

    // Post: n = 0
    void clear();

    // Pre: n > 0
    // Post: R = a[0]
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    Object element();

    // Post: R = n
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    int size();

    // Post: R = (n == 0)
    // n' = n && any i: 0 <= i < n => a[i]' = a[i]
    boolean isEmpty();

    // Pre: predicate != null
    // Post: queue' = a[0..n'-1]': exists sequence i_0,...,i_n'-1:
    // if 0 <= j1,j2 < n' && j1 < j2 -> i_j1 < i_j2:
    // every j: i_j: 0 <= i_j < n && predicate(a[i_j]) = false && a[i_j] = a[j]'
    // if predicate(a[k]) = false -> k in sequence i_j
    void removeIf(Predicate<Object> predicate);

    // Pre: predicate != null
    // Post: queue' = a[0..n'-1]': exists sequence i_0,...,i_n'-1:
    // if 0 <= j1,j2 < n' && j1 < j2 -> i_j1 < i_j2:
    // every i_j: 0 <= i_j < n && predicate(a[i_j]) = true && a[i_j] = a[j]'
    // if predicate(a[k]) = true -> k in sequence i_j
    void retainIf(Predicate<Object> predicate);

    // Pre: predicate != null
    // Post: queue' = a[0..n'-1]': every i: 0 <= i < n' && a[i] = a[i]' && predicate(a[i]) = true
    // (predicate(a[n']) = false || n' = n)
    void takeWhile(Predicate<Object> predicate);

    // Pre: predicate != null
    // Post: exists k: 0 <= k <= n && (k == n || predicate(a[k]) = false) && every i: 0 <= i < k -> predicate(a[i]) = true
    // n' = n - k && every i: 0 <= i < n' -> a[i]' = a[i + k]
    void dropWhile(Predicate<Object> predicate);
}
