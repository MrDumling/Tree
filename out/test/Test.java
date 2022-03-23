import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import tree.*;
import tree.dimensional.DimensionalPoint;

class Testing {

    @RepeatedTest(50)
    public void binaryTreeTests() {
        BinaryTree<DimensionalPoint, Float> binaryTree = new BinaryTree<>();
        binaryTreeAdd(binaryTree);

        Assertions.assertEquals(20, binaryTree.size());

        binaryTreeRemoveAndContains(binaryTree);

        Assertions.assertTrue(binaryTree.isTreeBase());
        Assertions.assertEquals(18, binaryTree.size());
        Assertions.assertEquals(82, testIterator(binaryTree));
    }

    public void binaryTreeAdd(BinaryTree<DimensionalPoint, Float> binaryTree) {
        for(int i = 0; i < 10; i++) {
            Assertions.assertTrue(binaryTree.add(new DimensionalPoint((float) i)));
            Assertions.assertTrue(binaryTree.add(new DimensionalPoint((float) i)));
        }
    }

    public void binaryTreeRemoveAndContains(BinaryTree<DimensionalPoint, Float> binaryTree) {
        Assertions.assertTrue(binaryTree.contains(new DimensionalPoint(4f)));
        Assertions.assertTrue(binaryTree.remove(new DimensionalPoint(4f)));
        Assertions.assertTrue(binaryTree.contains(new DimensionalPoint(4f)));
        Assertions.assertTrue(binaryTree.remove(new DimensionalPoint(4f)));

        Assertions.assertFalse(binaryTree.contains(new DimensionalPoint(4f)));
        Assertions.assertFalse(binaryTree.remove(new DimensionalPoint(4f)));
    }

    public int testIterator(BinaryTree<DimensionalPoint, Float> binaryTree) {
        int sum = 0;

        for(DimensionalPoint point : binaryTree) {
            sum += point.getDimension(0);
        }

        return sum;
    }
}