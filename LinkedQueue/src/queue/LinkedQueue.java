package queue;

import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private Node tail;
    private Node head;

    @Override
    public void enqueue(Object element) {
        if (head == null) {
            head = tail = new Node(element);
        } else {
            Node newHead = new Node(element);
            head.setNext(newHead);
            head = newHead;
        }
        size++;
    }

    @Override
    public Object dequeue() {
        Object val = tail.getElement();
        if (head == tail) {
            head = null;
        }
        tail = tail.getNext();
        size--;
        return val;
    }

    @Override
    public void clear() {
        size = 0;
        tail = head = null;
    }

    @Override
    public Object element() {
        return tail.getElement();
    }
}
