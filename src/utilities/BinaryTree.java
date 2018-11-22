package utilities;

public class BinaryTree<E extends Comparable<E>> {

    private Node<E> root;
    private int size = 0;


    public void add(E e) {
        root = insert(e, root);
    }

    public boolean contains(E e) {
        return (findNode(e, root) != null);
    }

    public E getLeftChild(E e) {
        Node<E> parent = findNode(e, root);
        if (parent == null || parent.left == null) {
            return null;
        }
        return parent.left.item;
    }

    public E getRightChild(E e) {
        Node<E> parent = findNode(e, root);
        if (parent == null || parent.right == null) {
            return null;
        }
        return parent.right.item;
    }

    public void remove(E e){
        root = delete(e, root);
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public int size() {
        return size;
    }


    //Privated Components
    private int getHeight(Node n) {
        return (n == null)? 0 : n.height;
    }

    private int getBalance(Node<E> n) {
        return (n == null)? 0 : getHeight(n.left) - getHeight(n.right);
    }

    private Node<E> insert(E e, Node<E> n) {
        if (n == null) {
            size++;
            return new Node<E>(e);
        }

        E o = n.item;
        if (e.compareTo(o) == 0) {
            return n;
        } else if (e.compareTo(o) < 0) {
            n.left = insert(e, n.left);
        } else {
            n.right = insert(e, n.right);
        }

        n.updateHeight();

        int balance = getBalance(n);
        if ((balance > 1) && (e.compareTo(n.left.item) < 0)) {
            return rotateRight(n);
        }
        if ((balance > 1) && (e.compareTo(n.left.item) > 0)) {
            n.left = rotateRight(n.left);
            return rotateRight(n);
        }
        if ((balance < -1) && (e.compareTo(n.right.item) < 0)) {
            n.right = rotateLeft(n.right);
            return rotateLeft(n);
        }
        if ((balance < -1) && (e.compareTo(n.right.item) > 0)) {
            return rotateLeft(n);
        }
        return n;
    }

    private Node<E> delete(E e, Node<E> n) {
        if (n == null) {
            return n;
        }

        E o = n.item;
        if (e.compareTo(o) == 0) {
            size--;

            //no children
            if (n.left == null && n.right == null) {
                return null;

                //no left child
            } else if (n.left == null) {
                n = n.right;

                //no right child
            } else if (n.right == null) {
                n = n.left;

                //two children
            } else {

                size++;
                Node<E> successor = n.right;
                while (successor.left != null) {
                    successor = successor.left;
                }

                n.item = successor.item;
                n.right = delete(successor.item, n.right);
            }

            return n;
        } else if (e.compareTo(o) < 0) {
            n.left = delete(e, n.left);
        } else {
            n.right = delete(e, n.right);
        }

        n.updateHeight();

        int balance = getBalance(n);
        if ((balance > 1) && (getBalance(n.left) >= 0)) {
            return rotateRight(n);
        }
        if ((balance > 1) && (getBalance(n.left) < 0)) {
            n.left = rotateRight(n.left);
            return rotateRight(n);
        }
        if ((balance < -1) && (getBalance(n.right) > 0)) {
            n.right = rotateLeft(n.right);
            return rotateLeft(n);
        }
        if ((balance < -1) && (getBalance(n.right) <= 0)) {
            return rotateLeft(n);
        }
        return n;
    }

    private Node<E> rotateLeft(Node<E> root) {
        Node<E> R = root.right;
        root.right = R.left;
        R.left = root;

        root.updateHeight();
        R.updateHeight();

        return R;
    }

    private Node<E> rotateRight(Node<E> root) {
        Node<E> L = root.left;
        root.left = L.right;
        L.right = root;

        root.updateHeight();
        L.updateHeight();

        return L;
    }

    private Node<E> findNode(E e, Node<E> n) {
        if (n == null) {
            return null;
        }

        E o = n.item;
        if (e.compareTo(o) == 0) {
            return n;
        } else if (e.compareTo(o) < 0) {
            return findNode(e, n.left);
        } else {
            return findNode(e, n.right);
        }
    }

    private class Node<T> {
        private T item;
        private int height = 1;
        private Node<T> left;
        private Node<T> right;

        private Node(T item) {
            this.item = item;
        }

        private void updateHeight() {
            height = Math.max(getHeight(left), getHeight(right)) + 1;
        }
    }
}