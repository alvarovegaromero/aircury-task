import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PuzzleSolver {
    static int width, height;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Write the name of the Puzzle you want to solve: ");
            String fileName  = scanner.nextLine();

            File puzzle = new File("puzzles/"+fileName);

            try (Scanner fileScanner = new Scanner(puzzle)) {
                width = fileScanner.nextInt();
                height = fileScanner.nextInt();
                fileScanner.nextLine();

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    System.out.println(line);
                }
                   
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while reading the input: " + e.getMessage());
        }
    }

    /*static void displaySolutions() {
        for (int[][] solution : solutions) {
            for (int[] row : solution) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
        }
    }*/
}