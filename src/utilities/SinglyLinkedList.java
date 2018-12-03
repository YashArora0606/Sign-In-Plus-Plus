package utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Singly linked list implementation
 * @param <E>
 */
public class SinglyLinkedList<E> implements Queue<E>, Iterable<E> {

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

    public int indexOf(E o) {
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

    public boolean remove(E o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        } else {
            remove(index);
            return true;
        }
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


    //Iterable methods
    @Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (E e : this) {
            action.accept(e);
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
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

    private class SinglyLinkedListIterator implements Iterator<E> {

        private Node<E> last = null;
        private Node<E> next = head;
        private int lastIndex = -1;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (next == null) {
                throw new NoSuchElementException();
            }

            last = next;
            next = next.next;
            lastIndex++;
            return last.item;
        }

        @Override
        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }

            SinglyLinkedList.this.remove(lastIndex);
            last = null;
            lastIndex--;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            while (hasNext()) {
                action.accept(next());
            }
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
