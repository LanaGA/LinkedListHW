package com.company;

import java.util.*;


public class MyLinkedList<E> implements List<Object> {

    private int size = 0;
    private Node<Object> first;
    private Node<Object> last;

    public static class Node<Object> {
        Object item;
        Node<Object> next;
        Node<Object> prev;

        Node(Node<Object> prev, Object element, Node<Object> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void insertLast(Object element) {
        Node<Object> lastNode = last;
        Node<Object> newNode = new Node<>(lastNode, element, null);
        last = newNode;
        if (lastNode == null)
            first = newNode;
        else {
            lastNode.next = newNode;
        }
        size++;
    }

    public void insertBefore(Object element, Node<Object> such) {
        Node<Object> preSuch = such.prev;
        Node<Object> newNode = new Node<>(preSuch, element, such);
        such.prev = newNode;
        if (preSuch == null)
            first = newNode;
        else
            preSuch.next = newNode;
        size++;
    }

    private Object unInsert(Node<Object> lastNode) {
        Object element = lastNode.item;
        Node<Object> next = lastNode.next;
        Node<Object> prev = lastNode.prev;

        if (prev == null)
            first = next;
        else {
            prev.next = next;
            lastNode.prev = null;
        }

        if (next == null)
            last = prev;
        else {
            next.prev = prev;
            lastNode.next = null;
        }
        lastNode.item = null;
        size--;
        return element;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<Object> firstNode = first; firstNode != null; firstNode = firstNode.next) {
                if (firstNode.item == null) {
                    unInsert(firstNode);
                    return true;
                }
            }
        } else {
            for (Node<Object> firstNode = first; firstNode != null; firstNode = firstNode.next) {
                if (o.equals(firstNode.item)) {
                    unInsert(firstNode);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object index : c) {
            if (!remove(index))
                return false;
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) > 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object index : c) {
            if (!contains(index))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<?> c) {
        for (Object index : c) {
            add(index);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        checkIndex(index);
        Node<Object> oldIndex = node(index);
        for (Object iter : c) {
            insertBefore(iter, oldIndex);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (Node i = first; i != null; i = i.next) {
            if (c.contains(i))
                continue;
            else
                remove(i);
        }
        return true;
    }

    @Override
    public void add(int index, Object element) {
        checkIndex(index);
        if (index == size) {
            insertLast(element);
        } else
            insertBefore(element, node(index));
    }

    @Override
    public boolean add(Object element) {
        insertLast(element);
        return true;
    }

    @Override
    public Object remove(int index) {
        checkIndex(index);
        return unInsert(node(index));
    }

    @Override
    public void clear() {
        for (Node i = first; i != null; ) {
            Node next = i.next;
            i.item = null;
            i.prev = null;
            i.next = null;
            i = next;
        }
        first = last = null;
        size = 0;

    }

    @Override
    public int indexOf(Object element) {
        int index = 0;
        if (element == null) {
            for (Node<Object> firstNode = first; firstNode != null; firstNode = firstNode.next, index++) {
                if (firstNode.item == null) {
                    return index;
                }
            }
        } else {
            for (Node<Object> firstNode = first; firstNode != null; firstNode = firstNode.next, index++) {
                if (element.equals(firstNode.item)) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int found = -1;
        int i = 0;
        for (Node<Object> firstNode = first; firstNode != null; firstNode = firstNode.next, i++) {
            if (firstNode == o)
                found = i;
        }
        return found;
    }

    @Override
    public ListIterator<Object> listIterator() {
        checkIndex(indexOf(first));
        return new LinkedListIteration(indexOf(first));
    }


    @Override
    public ListIterator<Object> listIterator(int index) {
        checkIndex(index);
        return new LinkedListIteration(index);
    }

    private class LinkedListIteration implements ListIterator<Object> {
        private Node<Object> prev;
        private Node<Object> next;
        private int nextIndex;

        LinkedListIteration(int index) {
            if (index == size)
                next = null;
            else
                next = node(index);
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public Object next() {
            if (!hasNext())
                throw new NoSuchElementException();

            prev = next;
            next = next.next;
            nextIndex++;
            return prev.item;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public Object previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();

            prev = next;
            if (next == null)
                next = last;
            else
                next = next.prev;
            nextIndex--;
            return prev.item;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (prev == null)
                throw new IllegalStateException();

            Node<Object> lastNext = prev.next;
            unInsert(prev);
            if (next == prev)
                next = lastNext;
            else
                nextIndex--;
            prev = null;
        }

        @Override
        public void set(Object e) {
            if (prev == null)
                throw new IllegalStateException();
            prev.item = e;

        }

        @Override
        public void add(Object e) {
            prev = null;
            if (next == null)
                insertLast(e);
            else
                insertBefore(e, next);
            nextIndex++;
        }
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        ArrayList<Object> sub = new ArrayList<>();
        Node<Object> startNode = node(fromIndex);
        for (int i = fromIndex; i < toIndex; i++, startNode = startNode.next) {
            sub.add(startNode.item);
        }
        return sub;
    }

    @Override
    public Object get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    @Override
    public Object set(int index, Object element) {
        checkIndex(index);
        Node<Object> nodeIndex = node(index);
        nodeIndex.item = element;
        return element;
    }

    public Object[] toArray() {
        int i = 0;
        Object[] result;
        result = (Object[]) java.lang.reflect.Array.newInstance(first.item.getClass(), size);
        for (Node<Object> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (result.length > size)
            result[size] = null;

        return result;
    }

    @Override
    public Object[] toArray(Object[] array) {
        if (array.length < size)
            array = (Object[]) java.lang.reflect.Array.newInstance(
                    array.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = array;
        for (Node<Object> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (array.length > size)
            array[size] = null;

        return array;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Object next() {
                return get(index++);
            }
        };
    }

    private void checkIndex(int index) {
        if (!(index >= 0 && index < size)) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    Node<Object> node(int index) {
        checkIndex(index);
        if (index < (size >> 1)) {
            Node<Object> indexFirstHalf = first;
            for (int i = 0; i < index; i++) {
                indexFirstHalf = indexFirstHalf.next;
            }
            return indexFirstHalf;
        } else {
            Node<Object> indexLastHalf = last;
            for (int i = size - 1; i > index; i--) {
                indexLastHalf = indexLastHalf.prev;
            }
            return indexLastHalf;
        }
    }

    @Override
    public String toString() {
        StringBuilder type = new StringBuilder();
        if(size()>0)
        for (Node<Object> firstNode = first; firstNode != last.next; firstNode = firstNode.next) {
            type.append(firstNode.item).append("\n");
        }
        return "MyLinkedList{" + "size=" + size + "}\n" + "Content:\n" + type;
    }
}
