package queue;

import java.util.function.Predicate;

import static java.util.function.Predicate.not;

abstract public class AbstractQueue implements Queue {
    protected int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void removeIf(Predicate<Object> predicate) {
        retainIf(not(predicate));
    }

    @Override
    public void dropWhile(Predicate<Object> predicate) {
        while (size > 0 && predicate.test(element())) {
            dequeue();
        }
    }

    @Override
    public void retainIf(Predicate<Object> predicate) {
        int sz = size;
        for (int i = 0; i < sz; i++) {
            Object element = dequeue();
            if (predicate.test(element)) {
                enqueue(element);
            }
        }
    }

    @Override
    public void takeWhile(Predicate<Object> predicate) {
        int sz = size;
        boolean take = true;
        for (int i = 0; i < sz; i++) {
            Object element = dequeue();
            if (!predicate.test(element)) {
                take = false;
            }
            if (take) {
                enqueue(element);
            }
        }
    }
}
