package tree.dimensional;

public abstract class Dimensional<T extends Comparable<T>> {
    public abstract T getDimension(int i) throws IllegalArgumentException;

    public int compareDimension(int i, Dimensional<T> comparedDimension) throws IllegalArgumentException {
        return getDimension(i).compareTo(comparedDimension.getDimension(i));
    }

    public abstract int getDimensionCount();

    @Override
    public boolean equals(Object checkedValue) {
        if(checkedValue == this) return true;

        if (checkedValue instanceof Dimensional checkedDimensional) {
            int dimensionCount = getDimensionCount();
            if (checkedDimensional.getDimensionCount() != dimensionCount) return false;

            for (int i = 0; i < dimensionCount; i++) {
                if (!getDimension(i).equals(checkedDimensional.getDimension(i))) return false;
            }

            return true;
        }

        return false;
    }

}