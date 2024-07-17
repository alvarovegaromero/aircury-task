import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PuzzleSolver {
    private static final int SIDES = 4;
    private static final int ROTATIONS = 4;

    static int width, height;
    static List<int[][]> pieces = new ArrayList<>();
    static List<int[][]> solutions = new ArrayList<>();

    public static void main(String[] args) {
        String fileName = getInput();

        try {
            processPuzzle(fileName);
            solutions = pieces; //to delete
            displaySolutions();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static String getInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Write the name of the Puzzle you want to solve: ");
            return scanner.nextLine();
        }
    }

    public static void processPuzzle(String fileName) throws FileNotFoundException {
        File puzzle = new File("puzzles/" + fileName);
    
        try (Scanner fileScanner = new Scanner(puzzle)) {
            width = fileScanner.nextInt();
            height = fileScanner.nextInt();
            fileScanner.nextLine();
    
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    int[] sides = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
                    pieces.add(generateAllRotations(sides));
                }
            }
        }
    }

    static int[][] generateAllRotations(int[] sides) {
        int[][] rotations = new int[ROTATIONS][SIDES];
        int[] current = sides.clone();
        for (int i = 0; i < ROTATIONS; i++) {
            rotations[i] = current.clone();
            current = rotatePiece(current);
        }
        return rotations;
    }

    static int[] rotatePiece(int[] sides) {
        return new int[]{sides[3], sides[0], sides[1], sides[2]};
    }

    static void displaySolutions() {
        System.out.println("Solutions:"); //to delete
        for (int[][] solution : solutions) {
            for (int[] row : solution) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
        }
    }
}