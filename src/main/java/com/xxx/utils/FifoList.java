package com.xxx.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FifoList<T extends Comparable> {
    private List<T> list;
    private int size;

    public FifoList(int size) {
        list = Collections.synchronizedList(new ArrayList<>(size));
        this.size = size;
    }

    public void add(T obj) {
        list.add(0, obj);
        if (list.size() > size) {
            list.remove(list.size() - 1);
        }
    }

    public String toString() {
        return list.toString();
    }

    public List<T> getList() {
        return new ArrayList<>(list);
    }

    public T getMax() {
        T max = null;
        for (T t : list) {
            if (max == null) {
                max = t;
            }
            if (max.compareTo(t) < 0) {
                max = t;
            }
        }
        return max;
    }

    public T getMin() {
        T min = null;
        for (T t : list) {
            if (min == null) {
                min = t;
            }
            if (min.compareTo(t) > 0) {
                min = t;
            }
        }
        return min;
    }

    public T get(int i) {
        return list.get(i);
    }

    public int listSize() {
        return list.size();
    }
}
