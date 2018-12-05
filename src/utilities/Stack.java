package utilities;

import java.util.EmptyStackException;

/**
 * Stack implementation
 * @param <E>
 */
public class Stack<E> {

    private SinglyLinkedList<E> stack = new SinglyLinkedList<>();

    public E push(E item) {
        stack.add(0, item);
        return item;
    }

    public E pop() {
        if (empty()) {
            throw new EmptyStackException();
        }
        return stack.remove(0);
    }

    public E peek() {
        if (empty()) {
            throw new EmptyStackException();
        }
        return stack.get(0);
    }

    public boolean empty() {
        return (stack.size() == 0);
    }

    public E get(int index) {
        return stack.get(index);
    }

    public int search(Object o) {
        int index = stack.indexOf(o);
        if (index == -1) {
            return -1;
        } else{
            return index + 1;
        }
    }

    public int size() {
        return stack.size();
    }
}
