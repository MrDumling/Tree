package tree;

import org.jetbrains.annotations.NotNull;
import tree.dimensional.Dimensional;

import java.util.Collections;
import java.util.Iterator;

public class KDTree<E extends Dimensional<T>, T extends Comparable<T>> extends Tree<E, T> {
    final int DIMENSION_COUNT;
    KDTree<E, T>[] branches = getEmptyBranches();

    protected KDTree(int dimensionCount) {
        this.DIMENSION_COUNT = dimensionCount;
    }

    KDTree(int dimensionCount, E rootValue) {
        this.DIMENSION_COUNT = dimensionCount;
        this.rootValue = rootValue;
    }

    KDTree<E, T>[] getEmptyBranches() {
        return new KDTree[1 << DIMENSION_COUNT];
    }

    int getContainingIndex(@NotNull E value) {
        int containingIndex = 0;

        for (int i = 0; i < DIMENSION_COUNT; i++) {
            if (rootValue.compareDimension(i, value) > 0) {
                containingIndex += 1 << i;
            }
        }

        return containingIndex;
    }

    boolean areBranchesEmpty() {
        for (KDTree<E, T> branch : branches) {
            if (branch != null) return false;
        }
        return true;
    }

    private Iterator<E> fullIterator() {
        return new Iterator<>() {
            boolean rootValueReturned = false;
            int branchIndex = -1; // will be set to at least 0 due to findNextIndex()
            Iterator<E> currentBranchIterator;

            {
                findNextIndex();
            }

            private void findNextIndex() {
                while (++branchIndex < branches.length) {
                    if (branches[branchIndex] != null) {
                        currentBranchIterator = branches[branchIndex].iterator();
                        return;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                if (!rootValueReturned) return true;
                return branchIndex < branches.length;
            }

            @Override
            public E next() {
                if (!rootValueReturned) {
                    rootValueReturned = true;
                    return rootValue;
                }

                E returnedValue = currentBranchIterator.next();
                if (!currentBranchIterator.hasNext()) {
                    findNextIndex();
                }

                return returnedValue;
            }
        };
    }

    @Override
    public Iterator<E> iterator() {
        if (!hasRootValue()) return Collections.emptyIterator();
        if (areBranchesEmpty()) return branchlessIterator();
        return fullIterator();
    }

    @Override
    public int size() {
        if (!hasRootValue()) return 0;
        int sum = 1;

        for (KDTree<E, T> branch : branches) {
            if (branch != null) sum += branch.size();
        }

        return sum;
    }

    @Override
    public boolean contains(@NotNull Object o) {
        if (o instanceof Dimensional)  {
            E checkedValue = (E)o;
            if (rootValue.equals(checkedValue)) return true;

            int containingIndex = getContainingIndex(checkedValue);

            if (branches[containingIndex] == null) return false;
            return branches[containingIndex].contains(o);
        }

        return false;
    }

    @Override
    public boolean add(E addedValue) {
        if (setRootValue(addedValue)) return true;

        int containingIndex = getContainingIndex(addedValue);

        if (branches[containingIndex] == null) {
            branches[containingIndex] = new KDTree<>(DIMENSION_COUNT, addedValue);
            branches[containingIndex].parentNode = this;
            return true;
        }
        return branches[containingIndex].add(addedValue);
    }

    protected void removeRootValue() {
        for (KDTree<E, T> currentBranch : branches) {
            if (currentBranch != null) {
                E branchRoot = currentBranch.rootValue;
                currentBranch.removeRootValue();
                rootValue = branchRoot;

                return;
            }
        }

        rootValue = null;
    }

    @Override
    public boolean remove(Object o) {
        if(this.rootValue == null) return false;

        if (o instanceof Dimensional) {
            if (rootValue.equals(o)) {
                removeRootValue();
                return true;
            }

            @SuppressWarnings("unchecked")
            E checkedValue = (E)o;
            int containingIndex = getContainingIndex(checkedValue);

            if (branches[containingIndex] == null) return false;

            boolean flag = branches[containingIndex].remove(o);
            if (!branches[containingIndex].hasRootValue()) branches[containingIndex] = null;
            return flag;
        }

        return false;
    }

    @Override
    public void clear() {
        if (isTreeBase()) { //clear node, ignore removing self from parent
            branches = getEmptyBranches();
            rootValue = null;
            return;
        }

        //remove self from parent
        KDTree<E, T> castParent = (KDTree<E, T>)parentNode;

        for(int i = 0; i < castParent.branches.length; i++) {
            if(castParent.branches[i] == this) {
                castParent.branches[i] = null;
                return;
            }
        }

    }
}
