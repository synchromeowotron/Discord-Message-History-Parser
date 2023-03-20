package org.example;

import java.util.LinkedList;

public class MaxSizeList<T> {

    private final LinkedList<T> list;
    private final int maxSize;

    public MaxSizeList(int maxSize) {
        this.list = new LinkedList<T>();
        this.maxSize = maxSize;
    }

    public void add(T item) {
        if (list.size() >= maxSize) {
            list.removeFirst();
        }
        list.addLast(item);
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        if (list.size() > 0) {
            list.clear();
        }
    }

    public LinkedList<T> getCurrentList() {
        return list;
    }
}
