package utilities;

import java.util.EmptyStackException;

/**
 * An implementation of a stack using {@link SinglyLinkedList}
 *
 * @param <E> the type of element held in this stack
 * @author Alston
 * last updated 12/5/2018
 */
public class Stack<E> {

    private SinglyLinkedList<E> stack = new SinglyLinkedList<>();

    /**
     * Pushes an item onto the top of the stack
     *
     * @param item the item to be pushed onto the stack
     * @return the item argument
     */
    public E push(E item) {
        stack.add(0, item);
        return item;
    }

    /**
     * Removes the item at the top of this stack and returns it
     *
     * @return the item at the top of this stack, or the removed object
     * @throws EmptyStackException thrown if the stack is empty
     */
    public E pop() {
        if (empty()) {
            throw new EmptyStackException();
        }
        return stack.remove(0);
    }

    /**
     * Looks at the item at the top of this stack, without removing it, and returns it
     *
     * @return the item at the top of this stack
     * @throws EmptyStackException thrown if the stack is empty
     */
    public E peek() {
        if (empty()) {
            throw new EmptyStackException();
        }
        return stack.get(0);
    }

    /**
     * Returns true if the stack is empty
     *
     * @return true if the stack is empty; false otherwise
     */
    public boolean empty() {
        return (stack.size() == 0);
    }

    /**
     * Returns the item at the specified index in the stack.
     * This stack is 0-indexed with the topmost item being index 0.
     *
     * @param index index of the item to search for
     * @return the item at the specified position in the stack
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E get(int index) {
        return stack.get(index);
    }

    /**
     * Returns the 1-based index of an object in the stack, where the index
     * is the distance from the top of the stack. The topmost item is considered to be index 1.
     * The reason why search(Object) is 1-based while get(int) is 0-based, is that search is designed
     * to mirror {@link java.util.Stack#search(Object)} in the Java Stack class 
     *
     * @param obj object to search for
     * @return the 1-based from the top of the stack where the object is located; -1 if the object is not in the stack
     */
    public int search(Object obj) {
        int index = stack.indexOf(obj);
        if (index == -1) {
            return -1;
        } else {
            return index + 1;
        }
    }

    /**
     * Returns the number of items in the stack
     *
     * @return the number of items in the stack
     */
    public int size() {
        return stack.size();
    }
}
