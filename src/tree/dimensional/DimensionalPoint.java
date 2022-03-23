package tree.dimensional;

public class DimensionalPoint extends Dimensional<Float> {
    Float value;

    public DimensionalPoint(Float value) {
        this.value = value;
    }

    @Override
    public Float getDimension(int i) throws IllegalArgumentException {
        if (i != 0) throw new IllegalArgumentException("Dimension must be 0");
        return value;
    }

    @Override
    public int getDimensionCount() {
        return 1;
    }
}
