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

    public void setSides(int[] sides) {
        this.sides = sides;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public String toString() {
        return "Index: "+ rowIndex + " - Content: "+Arrays.toString(sides);
    }
}