import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PuzzleSolver {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Write the name of the Puzzle you want to solve:");
            String fileName  = scanner.nextLine();

            File puzzle = new File("puzzles/"+fileName);

            try (Scanner fileScanner = new Scanner(puzzle)) {
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
}