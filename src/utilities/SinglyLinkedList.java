package utilities;

import java.util.NoSuchElementException;

/**
 * Singly linked list implementation
 * @param <E>
 */
public class SinglyLinkedList<E> implements Queue<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;


    // LinkedList Methods
    public boolean add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> inserted = new Node<>(e);
        if (index == 0) {
            inserted.next = head;
            head = inserted;
        } else {
            Node<E> prev = getNode(index-1);
            inserted.next = prev.next;
            prev.next = inserted;
        }
        if (index == size) {
            tail = inserted;
        }

        size++;
        return true;
    }

    public boolean add(E e) {
        return add(size, e);
    }

    public E get(int index) {
        return getNode(index).item;
    }

    public int indexOf(Object o) {
        Node<E> node = head;
        for (int i = 0; i < size; i++) {
            if (o == node.item || o.equals(node.item)) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> prev;
        Node<E> removed;
        if (index == 0) {
            prev = null;
            removed = head;
            head = head.next;
        } else {
            prev = getNode(index-1);
            removed = prev.next;
            prev.next = removed.next;
        }
        if (index == size-1) {
            tail = prev;
        }

        size--;
        return removed.item;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        } else {
            remove(index);
            return true;
        }
    }

    public static SinglyLinkedList copyOf(SinglyLinkedList list){
        SinglyLinkedList newList = new SinglyLinkedList<>();
        for (int i = 0; i < list.size(); i++){
            newList.add(list.get(i));
        }
        return newList;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }


    // Queue Methods
    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return remove(0);
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        return remove(0);
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return get(0);
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return get(0);
    }


    // Privated Components
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            return head;

        } else if (index == size-1) {
            return tail;

        } else {
            Node<E> node = head;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        }
    }

    private class Node<T> {
        private T item;
        private Node<T> next;

        Node(T item) {
            this.item = item;
        }

        Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }
}
