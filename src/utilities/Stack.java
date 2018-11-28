package utilities;

public class Stack<E> {
    private StackNode<E> head;

    public E pop() {
        E value = head.getItem();
        StackNode<E> temp = head.getNext();
        head = null;
        head = temp;
        return value;
    }

    public void push(E item) {
        StackNode<E> temp = new StackNode<>(item);
        temp.setNext(head);
        head = temp;
    }

    public int size() {
        int size = 1;
        if (head.getNext() == null) {
            return size;
        } else {
            StackNode<E> buffer = head;
            while (buffer.getNext() != null) {
                size++;
                buffer = buffer.getNext();

            }
            return size;
        }
    }

    public E get(int index) {
        StackNode<E> buffer = head;
        for (int i = 0; i < index; i++) {
            buffer = buffer.getNext();
        }
        return buffer.getItem();
    }


    private class StackNode<E> {
        private E item;
        private StackNode<E> next;

        public StackNode(E item) {
            this.item = item;
            this.next = null;
        }

        public StackNode(E item, StackNode<E> next) {
            this.item = item;
            this.next = next;
        }

        public StackNode<E> getNext() {
            return this.next;
        }

        public void setNext(StackNode<E> next) {
            this.next = next;
        }

        public E getItem() {
            return this.item;
        }
    }
}

