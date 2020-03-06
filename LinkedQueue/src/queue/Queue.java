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

    void removeIf(Predicate<Object> predicate);

    void retainIf(Predicate<Object> predicate);

    void takeWhile(Predicate<Object> predicate);

    void dropWhile(Predicate<Object> predicate);
}
