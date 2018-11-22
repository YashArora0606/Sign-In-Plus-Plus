package utilities;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Min-priority queue heap implementation
 * Thus, it is faster than the linked list implementation
 * @param <E>
 */
public class FastPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private ArrayList<E> heap = new ArrayList<>();


    //Queue Methods
    @Override
    public boolean add(E e) {
        heappush(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        heappush(e);
        return true;
    }

    @Override
    public E remove() {
        if (heap.size() == 0) {
            throw new NoSuchElementException();
        }

        E min = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        heapify(0);
        return min;
    }

    @Override
    public E poll() {
        if (heap.size() == 0) {
            return null;
        }

        E min = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        heapify(0);
        return min;
    }

    @Override
    public E element() {
        if (heap.size() == 0) {
            throw new NoSuchElementException();
        }
        return heap.get(0);
    }

    @Override
    public E peek() {
        if (heap.size() == 0) {
            return null;
        }
        return heap.get(0);
    }

    @Override
    public int size() {
        return heap.size();
    }


    //Heap methods
    private int parent(int i) {
        return (i - 1)/2;
    }

    private int left(int i) {
        return (i << 1) + 1;
    }

    private int right(int i) {
        return (i << 1) + 2;
    }

    private void heappush(E e) {
        heap.add(e);
        int i = heap.size() - 1;
        int parent = parent(i);

        while ((parent != i) && (e.compareTo(heap.get(parent)) < 0)) {
            swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    private void heapify(int i) {
        int left = left(i);
        int right = right(i);
        int smallest = i;

        if ((left < heap.size()) && (heap.get(left).compareTo(heap.get(smallest)) < 0) ) {
            smallest = left;
        }
        if ((right < heap.size()) && (heap.get(right).compareTo(heap.get(smallest)) < 0)) {
            smallest = right;
        }

        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    private void swap(int a, int b) {
        E tmp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, tmp);
    }
}
