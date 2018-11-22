package utilities;

import java.util.NoSuchElementException;

/**
 * Min-priority queue linked list implementation
 * Thus, it is slower :( (and my soul is slightly damaged)
 * At least, I tried to make it stable
 * @param <E>
 */
public class SlowPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private Node<E> head;
    private int size = 0;


    // Queue Methods
    @Override
    public boolean add(E e) {
        insert(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        insert(e);
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return extractMin();
    }

    @Override
    public E poll() {
        return extractMin();
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return getMin();
    }

    @Override
    public E peek() {
        return getMin();
    }

    @Override
    public int size() {
        return size;
    }


    // Privated Components
    private void insert(E e) {
        Node<E> prev = null;
        Node<E> curr = head;
        while ((curr != null) && ((e.compareTo(curr.item) > 0))) {
            prev = curr;
            curr = curr.next;
        }

        if (prev == null) {
            head = new Node<>(e, head);
        } else {
            prev.next = new Node<>(e, prev.next);
        }
        size++;
    }

    private E getMin() {
        if (head == null) {
            return null;
        }
        return head.item;
    }

    private E extractMin() {
        if (head == null) {
            return null;
        }

        E min = head.item;
        head = head.next;
        size--;
        return min;
    }

    private class Node<T> {
        private T item;
        private Node<T> next;

        public Node(T item) {
            this.item = item;
        }

        public Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }
}
