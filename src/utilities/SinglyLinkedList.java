package utilities;

public class SinglyLinkedList<T> {
    private Node<T> head;
    private int size = 0;

    SinglyLinkedList() {
    }

    public boolean add(T data) {
        return add(size, data);
    }

    public boolean add(int index, T data) {
        if ((index > size) || (index < 0)) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<>(data);
        if (index == 0) {
            newNode.setNext(head);
            head = newNode;
        } else {
            Node<T> previous = getNode(index - 1);
            newNode.setNext(previous.next());
            previous.setNext(newNode);
        }
        size++;
        return true;
    }


    public void clear() {
        head = null;
        size = 0;
    }

    public boolean contains(T data) {
        return (indexOf(data) >= 0);
    }

    public T get(int index) {
        return getNode(index).getData();
    }

    public int indexOf(T data) {
        Node<T> temp = head;
        for (int i = 0; i < size; i++) {
            if ((temp.getData().equals(data)) || (temp.getData() == data)) {
                return i;
            }
            temp = temp.next();
        }
        return -1;
    }

    public boolean isEmpty() {
        return (head == null);
    }

    public int lastIndexOf(T data) {
        Node<T> temp = head;
        int index = -1;
        for (int i = 0; i < size; i++) {
            if ((temp.getData().equals(data)) || (temp.getData() == data)) {
                index = i;
            }
            temp = temp.next();
        }
        return index;
    }

    public T remove(int index) {
        if ((index >= size) || (index < 0)) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> removed;
        if (index == 0) {
            removed = head;
            if (size > 1) {
                head = head.next();
            } else {
                head = null;
            }
        } else {
            Node<T> temp = getNode(index - 1);
            removed = temp.next();
            temp.setNext(temp.next().next());
        }
        size--;
        return removed.getData();
    }

    public boolean remove(T data) {
        int index = indexOf(data);
        if (index == -1) {
            return false;
        } else {
            remove(indexOf(data));
            return true;
        }
    }

    public int size() {
        return size;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next();
        }
        return temp;
    }

    private class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
        }

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        public T getData() {
            return this.data;
        }

        public Node<T> next() {
            return this.next;
        }

        public void setData(T data) {
            this.data = data;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }
}
