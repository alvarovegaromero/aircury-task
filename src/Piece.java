import java.util.Arrays;

public class Piece {
    private int[] sides;
    private int rowIndex;

    public Piece(int[] sides, int rowIndex) {
        this.sides = sides;
        this.rowIndex = rowIndex;
    }

    public int[] getSides() {
        return sides;
    }

    public int getSide(int index) {
        return sides[index];
    }

    public void setSides(int[] sides) {
        this.sides = sides;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void rotate() {
        int lastElement = sides[sides.length - 1];
        System.arraycopy(sides, 0, sides, 1, sides.length - 1);
        sides[0] = lastElement;
    }

    @Override
    public String toString() {
        return "Index: "+ rowIndex + " - Content: "+Arrays.toString(sides);
    }
}