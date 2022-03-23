package tree;

import tree.dimensional.Dimensional;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public abstract class Tree<E extends Dimensional<T>, T extends Comparable<T>> extends AbstractCollection<E> {
    protected E rootValue;
    protected Tree<E, T> parentNode;

    public boolean setRootValue(E addedValue) {
        if (rootValue == null) {
            rootValue = addedValue;
            return true;
        }

        return false;
    }

    public boolean isTreeBase() {
        return parentNode == null;
    }

    final E getRootValue() {
        return rootValue;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean hasRootValue() {
        return rootValue != null;
    }

    protected final Iterator<E> branchlessIterator() {
        return new Iterator<>() {
            boolean rootValueReturned = false;

            @Override
            public boolean hasNext() {
                return !rootValueReturned;
            }

            @Override
            public E next() {
                rootValueReturned = true;
                return getRootValue();
            }
        };
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modifiedFlag = false;

        Object[] valueArray = toArray();

        for (Object currentValue : valueArray) {
            if (!c.contains(currentValue)) {
                remove(currentValue);
                modifiedFlag = true;
            }
        }

        return modifiedFlag;
    }

    protected abstract void removeRootValue();

    @Override
    public abstract boolean contains(Object o);

    @Override
    public abstract boolean add(E e);

    @Override
    public abstract boolean remove(Object o);

    @Override
    public abstract void clear();

    @Override
    public boolean isEmpty() {
        return rootValue == null;
    }
}
