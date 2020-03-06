package queue;

import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private int tail;
    private Object[] elements;

    public ArrayQueue() {
        elements = new Object[2];
    }

    @Override
    public void enqueue(Object element) {
        elements[(tail + size) % elements.length] = element;
        size++;
        if (size == elements.length) {
            resize();
        }
    }

    @Override
    public Object dequeue() {
        size--;
        Object val = elements[tail];
        elements[tail] = null;
        tail = (tail + 1) % elements.length;
        if (size < elements.length / 4) {
            resize();
        }
        return val;
    }

    @Override
    public void retainIf(Predicate<Object> predicate) {
        Object[] nw = new Object[elements.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(elements[(tail + i) % elements.length])) {
                nw[newSize++] = elements[(tail + i) % elements.length];
            }
        }
        elements = nw;
        tail = 0;
        size = newSize;
    }

    @Override
    public void takeWhile(Predicate<Object> predicate) {
        for (int i = 0; i < size; i++) {
            if (!predicate.test(elements[(tail + i) % elements.length])) {
                for (int j = i + 1; j < size; j++) {
                    elements[(tail + i) % elements.length] = null;
                }
                size = i + 1;
                break;
            }
        }
    }

    @Override
    public void clear() {
        elements = new Object[2];
        tail = size = 0;
    }

    @Override
    public Object element() {
        return elements[tail];
    }

    private void resize() {
        Object[] nw = new Object[(size + 1) * 2];
        if (tail + size < elements.length) {
            System.arraycopy(elements, tail, nw, 0, size);
        }   else {
            System.arraycopy(elements, tail, nw, 0, elements.length - tail);
            System.arraycopy(elements, 0, nw, elements.length - tail, (tail + size) % elements.length);
        }
        tail = 0;
        elements = nw;
    }
}
