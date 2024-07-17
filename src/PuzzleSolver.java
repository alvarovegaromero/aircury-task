import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PuzzleSolver {
    private static final int SIDES = 4;
    private static final int ROTATIONS = 4;

    private static int width, height;
    //private static List<int[][]> pieces = new ArrayList<>();
    private static List<Piece> pieces = new ArrayList<>();
    private static List<int[][]> solutions = new ArrayList<>();

    public static void main(String[] args) {
        try {
            String fileName = getFilenameInput();
            processFile(fileName);
            solvePuzzle();
            displaySolutions();
        } catch(Exception e) {
            System.out.println("Error during program execution - " + e.getMessage());
        }
    }

    private static String getFilenameInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Write the name of the Puzzle you want to solve: ");
            String fileName = scanner.nextLine();
            if (fileName.isEmpty()) {
                throw new IllegalArgumentException("File name cannot be empty");
            }
            return fileName;
        }
    }

    private static void processFile(String fileName) {
        File puzzle = new File("puzzles/" + fileName);

        try (Scanner fileScanner = new Scanner(puzzle)) { 
            width = fileScanner.nextInt();
            height = fileScanner.nextInt();
            fileScanner.nextLine();
            
            int i = 1;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    int[] sides = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
                    pieces.add(new Piece(sides, i));
                    //pieces.add(generateAllRotations(sides));
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The specified file could not be found: " + fileName);
        }
    }
/*
    private static int[][] generateAllRotations(int[] sides) {
        int[][] rotations = new int[ROTATIONS][SIDES];
        int[] current = sides.clone();
        for (int i = 0; i < ROTATIONS; i++) {
            rotations[i] = current.clone();
            current = rotatePiece(current);
        }
        return rotations;
    }

    private static int[] rotatePiece(int[] sides) {
        if (sides.length != SIDES) {
            throw new IllegalArgumentException("The input array must have " + SIDES + " elements.");
        }
        int[] rotated = new int[SIDES];
        rotated[0] = sides[SIDES - 1];
        for (int i = 1; i < SIDES; i++) {
            rotated[i] = sides[i - 1];
        }
        return rotated;
    }
*/
    private static void solvePuzzle() {
        int[][] board = new int[height][width]; // Create an empty board
        
        solveRecursive(board, new ArrayList<>(pieces), 0, 0); // Start solving the puzzle
    }
        
    private static void solveRecursive(int[][] board, List<Piece> remainingPieces, int row, int col) {
        if (row == height) { // If we have filled the board.
            int[][] solution = new int[height][width];
            for (int i = 0; i < height; i++) {
                solution[i] = board[i].clone();
            }
            solutions.add(solution); // Add the solution
            return;
        }
        
        if (col == width) { // If we have reached the end of a row
            solveRecursive(board, remainingPieces, row + 1, 0); // Move to the next row
            return;
        }
    
        // Fill row by row
        for (int i = 0; i < remainingPieces.size(); i++) {
            Piece piece = remainingPieces.get(i);
    
            if (pieceFits(board, piece, row, col)) { // If the piece fits
                placePiece(board, piece, row, col); // Place the piece on the board
    
                // Remove the piece from the remaining pieces and continue with the next position
                List<Piece> newRemainingPieces = new ArrayList<>(remainingPieces);
                newRemainingPieces.remove(i);
                solveRecursive(board, newRemainingPieces, row, col + 1);
    
                removePiece(board, row, col); // No solution - prune - remove from board
            }
        }
    }

    private static boolean pieceFits(int[][] board, Piece piece, int row, int col) {
        // Check if the piece fits within the board
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return false;
        }
    
        // Check if the position on the board is already occupied
        if (board[row][col] != 0) {
            return false;
        }
    
        // Check if the piece fits with the surrounding pieces
        // Assuming that the Piece class has a getSides() method that returns an array of integers representing the sides
        int[] sides = piece.getSides();
    
        // Check the piece above
        if (row > 0 && board[row - 1][col] != 0 && board[row - 1][col] != sides[0]) {
            return false;
        }
    
        // Check the piece to the right
        if (col < board[0].length - 1 && board[row][col + 1] != 0 && board[row][col + 1] != sides[1]) {
            return false;
        }
    
        // Check the piece below
        if (row < board.length - 1 && board[row + 1][col] != 0 && board[row + 1][col] != sides[2]) {
            return false;
        }
    
        // Check the piece to the left
        if (col > 0 && board[row][col - 1] != 0 && board[row][col - 1] != sides[3]) {
            return false;
        }
    
        // If all checks passed, the piece fits
        return true;
    }

    private static void placePiece(int[][] board, Piece piece, int row, int col) {
        // Asumiendo que cada pieza tiene un ID único
        board[row][col] = piece.getRowIndex();
    }
    
    private static void removePiece(int[][] board, int row, int col) {
        // Asumiendo que un valor de 0 representa un espacio vacío en el tablero
        board[row][col] = 0;
    }

    private static void displaySolutions() {
        System.out.println("Solutions:");
        for (int[][] solution : solutions) {
            for (int[] row : solution) {
                String rowStr = Arrays.stream(row)
                                      .mapToObj(Integer::toString)
                                      .collect(Collectors.joining(", "));
                System.out.println(rowStr);
            }
            System.out.println(); // Print a blank line between solutions
        }
    }
}