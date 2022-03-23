package tree.dimensional;

public class Position2D extends Dimensional<Float> {
    Float x, y;

    public Position2D(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Float getDimension(int dimension) throws IllegalArgumentException {
        if (dimension == 0) return x;
        if (dimension == 1) return y;
        
        throw new IllegalArgumentException("dimension must be 1 or 2");
    }

    public int getDimensionCount() {
        return 2;
    }
}