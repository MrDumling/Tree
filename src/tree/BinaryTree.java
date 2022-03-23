package tree;

import org.jetbrains.annotations.NotNull;

import tree.dimensional.Dimensional;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class BinaryTree<E extends Dimensional<T>, T extends Comparable<T>> extends Tree<E, T> {
    private BinaryTree<E, T> left, right;

    public BinaryTree() {
    }

    public BinaryTree(E rootValue) {
        this.rootValue = rootValue;
    }

    @Override
    public int size() {
        if (!hasRootValue()) return 0;
        int sum = 1;

        if (left != null) sum += left.size();
        if (right != null) sum += right.size();

        return sum;
    }

    @Override
    public boolean contains(@NotNull Object o) {
        if (o instanceof Dimensional) {
            E checkedPoint = (E) o;
            if (rootValue.equals(checkedPoint)) return true;

            if (rootValue.compareDimension(0, checkedPoint) > 0) {
                if (left == null) return false;
                return left.contains(o);
            }

            if (right == null) return false;
            return right.contains(o);
        }

        return false;
    }

    private Iterator<E> singleBranchIterator() {
        return new Iterator<>() {
            boolean retrievedValue = false;
            final Iterator<E> branchIterator;

            {
                if (left == null) {
                    branchIterator = right.iterator();
                } else {
                    branchIterator = left.iterator();
                }
            }

            @Override
            public boolean hasNext() {
                return !retrievedValue || branchIterator.hasNext();
            }

            @Override
            public E next() {
                if (!retrievedValue) {
                    retrievedValue = true;
                    return getRootValue();
                }

                return branchIterator.next();
            }
        };
    }

    private Iterator<E> fullIterator() {
        return new Iterator<>() {
            boolean rootValueReturned = false;
            boolean leftFinished = false;

            final Iterator<E> leftIterator = left.iterator();
            final Iterator<E> rightIterator = right.iterator();

            @Override
            public boolean hasNext() {
                return !rootValueReturned || !leftFinished || rightIterator.hasNext();
            }

            @Override
            public E next() {
                if (!rootValueReturned) {
                    rootValueReturned = true;
                    return getRootValue();
                }

                if (!leftFinished) {
                    E returnedValue = leftIterator.next();
                    if (!leftIterator.hasNext()) leftFinished = true;
                    return returnedValue;
                }

                return rightIterator.next();
            }
        };
    }

    @Override
    public Iterator<E> iterator() {
        if (!hasRootValue()) return Collections.emptyIterator();
        if (left == null && right == null) return branchlessIterator();
        if (left == null ^ right == null) return singleBranchIterator();
        return fullIterator();
    }

    @Override
    public boolean add(@NotNull final E addedValue) {
        if (setRootValue(addedValue)) return true;

        if (rootValue.compareDimension(0, addedValue) > 0) {
            if (left == null) {
                left = new BinaryTree<>(addedValue);
                left.parentNode = this;
                return true;
            }
            return left.add(addedValue);
        }

        if (right == null) {
            right = new BinaryTree<>(addedValue);
            right.parentNode = this;
            return true;
        }

        return right.add(addedValue);
    }

    @Override
    protected void removeRootValue() {
        if (left != null) {
            E leftRoot = left.rootValue;
            left.removeRootValue();
            rootValue = leftRoot;
            return;
        }

        if (right != null) {
            E rightRoot = right.rootValue;
            right.removeRootValue();
            rootValue = rightRoot;
            return;
        }

        rootValue = null;
    }

    @Override
    public boolean remove(@NotNull Object o) {
        if (o instanceof Dimensional<?> checkedPoint) {
            if (rootValue.equals(checkedPoint)) {
                removeRootValue();
                return true;
            }

            //noinspection unchecked
            if (rootValue.compareDimension(0, (E) checkedPoint) > 0) {
                if (left == null) return false;

                boolean flag = left.remove(o);
                if (!left.hasRootValue()) left = null;
                return flag;
            }

            if (right == null) return false;

            boolean flag = right.remove(o);
            if (!right.hasRootValue()) right = null;
            return flag;
        }

        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modifiedFlag = false;

        for (Object removedValue : c) {
            if (remove(removedValue)) modifiedFlag = true;
        }

        return modifiedFlag;
    }

    @Override
    public void clear() {
        if (isTreeBase()) {
            left = null;
            right = null;
            rootValue = null;

            return;
        }

        BinaryTree<E, T> castParent = (BinaryTree<E, T>) parentNode;

        if (castParent.left == this) {
            castParent.left = null;
        } else {
            castParent.right = null;
        }

    }

}
