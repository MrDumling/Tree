package tree.dimensional;

public class KD_Dimensional<T extends Comparable<T>> extends Dimensional<T> {

    final int SIZE;
    T[] values;

    KD_Dimensional(int size) {
        this.SIZE = size;
        values = (T[]) new Object[size];
    }

    @SafeVarargs
    KD_Dimensional(T ... values) {
        this.values = values;
        this.SIZE = values.length;
    }

    @SafeVarargs
    KD_Dimensional(int size, T ... values) {
        this.values = values;
        this.SIZE = size;
    }

    @Override
    public T getDimension(int i) throws IllegalArgumentException {
        if(i < 0) throw new IllegalArgumentException("Requested dimension must be above 0");
        if(i >= SIZE) throw new IllegalArgumentException("Requested dimension must be below " + SIZE);

        return values[i];
    }

    @Override
    public int getDimensionCount() {
        return SIZE;
    }

    void setDimension(int dimension, T setValue) {
        values[dimension] = setValue;
    }
}
