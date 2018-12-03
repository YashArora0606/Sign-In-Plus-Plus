package utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A singly-linked list
 * The methods are closely parallel to that of {@link java.util.LinkedList}, thus, much of the JavaDocs are copied
 * In addition, the list is unable to deal with a size greater than Integer.MAX_VALUE; however,
 * populating the list with this many items is anticipated to never happen. The list is expected to run
 * on a single thread so {@link java.util.ConcurrentModificationException} errors are not accounted for either
 *
 * @param <E> the type of elements held in the list
 * @author Alston
 * last updated 12/3/2018
 */
public class SinglyLinkedList<E> implements Iterable<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;


    /**
     * Inserts the specified element at the specified position in the list.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     */
    public void add(int index, E element) {
        if (index < 0 || index > size) { //check if index is out of bounds
            throw new IndexOutOfBoundsException();
        }

        Node<E> inserted = new Node<>(element);

        if (index == 0) { //if the element is inserted at the head
            inserted.next = head;
            head = inserted;

        } else {
            Node<E> prev = getNode(index - 1); //get the node previous to the index
            inserted.next = prev.next;
            prev.next = inserted;
        }

        if (index == size) { //update the tail reference if needed
            tail = inserted;
        }

        size++; //increment size
    }

    /**
     * Appends the specified element to the end of the list
     *
     * @param e element to be appended to the list
     * @return true
     */
    public boolean add(E e) {
        add(size, e);
        return true;
    }

    /**
     * Returns the element at the specified position in the list
     *
     * @param index index of the element to return
     * @return the element at the specified position in the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E get(int index) {
        return getNode(index).item;
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1, if this list does not contain the element. More formally, returns the highest
     * index i such that (o == get(i) || o.equals(get(i))), or -1 if no such index exists
     *
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in this list, or -1 otherwise
     */
    public int indexOf(Object o) {
        Node<E> node = head;
        for (int i = 0; i < size; i++) { //iterate through the list, checking each item
            if (o == node.item || o.equals(node.item)) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left. Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified location
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E remove(int index) {
        if (index < 0 || index >= size) { //check if index is out of bounds
            throw new IndexOutOfBoundsException();
        }

        Node<E> prev;
        Node<E> removed;

        if (index == 0) { //if the head is deleted
            prev = null;
            removed = head;
            head = head.next;

        } else {
            prev = getNode(index - 1); //get the node previous to the one to be deleted
            removed = prev.next;
            prev.next = removed.next;
        }

        if (index == size - 1) { //update tail if needed
            tail = prev;
        }

        size--; //increment size

        return removed.item;
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     * If this list does not contain the element, it is unchanged. Returns true if this list
     * contained the specified element. More formally, removes the element with the lowest
     * index i such that (o == get(i) || o.equals(get(i))) if such an element exists.
     *
     * @param o element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public boolean remove(Object o) {
        int index = indexOf(o); //get the index of the element
        if (index == -1) {
            return false;
        } else {
            remove(index);
            return true;
        }
    }

    /**
     * Removes all the elements from this list.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list.
     */
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence
     *
     * @return an Iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator();
    }

    /**
     * Performs the given action for each element of the Iterable until
     * all elements have been processed or the action throws an exception.
     *
     * @param action the action to be performed for each element
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        for (E e : this) {
            action.accept(e);
        }
    }

    /**
     * @return
     * @throws UnsupportedOperationException spliterators are beyond the scope of this data structure
     */
    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the node at the specified position in the list
     *
     * @param index index of the element to return
     * @return the node at the specified position in the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) { //check if index is out of bounds
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) { //for quick retrieval, check if node is the head...
            return head;

        } else if (index == size - 1) { //...or tail of the list
            return tail;

        } else {
            Node<E> node = head;
            for (int i = 0; i < index; i++) { //iterate through the list to find the node
                node = node.next;
            }
            return node;
        }
    }

    /**
     * Iterator for this list
     */
    private class SinglyLinkedListIterator implements Iterator<E> {

        private Node<E> last = null; //previously returned node
        private int lastIndex = -1; //index of the previously returned node

        private Node<E> next = head; //next node

        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next element in the iteration
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            if (next == null) { //check if there is another element
                throw new NoSuchElementException();
            }

            last = next;
            next = next.next;
            lastIndex++;
            return last.item;
        }

        /**
         * Removes the last element returned by this iterator
         *
         * @throws IllegalStateException if the next method has not yet been called, or the remove
         *                               method has already been called after the last call to the next method
         */
        @Override
        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }

            SinglyLinkedList.this.remove(lastIndex);
            last = null;
            lastIndex--;
        }

        /**
         * Performs the given action for each remaining element until
         * all elements have been processed or the action throws an exception.
         *
         * @param action the action to be performed for each element
         */
        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }
    }

    /**
     * Represents the linked nodes that make up the list
     *
     * @param <T> the type of element held within the node
     */
    private class Node<T> {
        private T item;
        private Node<T> next;

        /**
         * Constructs a node that holds a specified item
         *
         * @param item the specified item
         */
        private Node(T item) {
            this.item = item;
        }
    }
}
