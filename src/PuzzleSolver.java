import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PuzzleSolver {
    private static final int ROTATIONS = 4;
    private static final int BORDER = 0;

    private static int width, height;
    private static final List<Piece> pieces = new ArrayList<>();
    private static final List<int[][]> solutions = new ArrayList<>();

    public static void main(String[] args) {
        try {
            String fileName = getPuzzleFilenameFromUser();
            loadPuzzleFromFile(fileName);
            solvePuzzle();
            displaySolutions();
        } catch(Exception e) {
            System.out.println("Error during program execution - " + e.getMessage());
        }
    }

    private static String getPuzzleFilenameFromUser() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Write the name of the Puzzle you want to solve: ");
            String fileName = scanner.nextLine();
            if (fileName.isEmpty()) {
                throw new IllegalArgumentException("File name cannot be empty");
            }
            return fileName;
        }
    }

    private static void loadPuzzleFromFile(String fileName) {
        File puzzle = new File("puzzles/" + fileName);

        try (Scanner fileScanner = new Scanner(puzzle)) { 
            width = fileScanner.nextInt();
            height = fileScanner.nextInt();
            fileScanner.nextLine();
            
            int rowIndex = 1;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    int[] sides = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
                    pieces.add(new Piece(sides, rowIndex));
                }
                rowIndex++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The specified file could not be found: " + fileName);
        }
    }

    private static void solvePuzzle() {
        Piece[][] board = new Piece[height][width]; // empty board - all null
        
        solveRecursive(board, new ArrayList<>(pieces), 0, 0);
    }
        
    private static void solveRecursive(Piece[][] board, List<Piece> remainingPieces, int row, int col) {
        if (row == height) {
            handlePossibleSolution(board);
            return;
        }
    
        if (col == width) { //End of current row. Move to next one
            solveRecursive(board, remainingPieces, row + 1, 0);
            return;
        }
        
        // Fill column by column and once finished the row, move to the next one
        tryAllPieces(board, remainingPieces, row, col);
    }

    private static void handlePossibleSolution(Piece[][] board) {
        int[][] solution = new int[height][width];
        
        for (int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                solution[i][j] = board[i][j].getRowIndex();
            }
        }
        
        if (!isRotationOfExistingSolution(solution)) {
            solutions.add(solution);
        }
    }

    private static boolean isRotationOfExistingSolution(int[][] solution) {
        for (int[][] existingSolution : solutions) {
            for (int i = 0; i < ROTATIONS; i++) {
                if (Arrays.deepEquals(solution, existingSolution)) {
                    return true;
                }
                existingSolution = rotateMatrix(existingSolution);
            }
        }
        return false;
    }

    private static int[][] rotateMatrix(int[][] matrix) {
        int[][] rotatedMatrix = new int[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                rotatedMatrix[i][j] = matrix[width-j-1][i];
            }
        }
        return rotatedMatrix;
    }

    private static void tryAllPieces(Piece[][] board, List<Piece> remainingPieces, int row, int col) {
        for (int i = 0; i < remainingPieces.size(); i++) {
            Piece piece = remainingPieces.get(i);
    
            for (int rotation = 0; rotation < ROTATIONS; rotation++) {
                piece.rotate();
    
                if (pieceFits(board, piece, row, col)) {
                    placePieceAndContinue(board, piece, remainingPieces, i, row, col);
                    removePiece(board, row, col); // No solution found, remove piece
                }
            }
        }
    }

    private static void placePieceAndContinue(Piece[][] board, Piece piece, List<Piece> remainingPieces, int i, int row, int col) {
        placePiece(board, piece, row, col);
    
        List<Piece> newRemainingPieces = new ArrayList<>(remainingPieces);
        newRemainingPieces.remove(i);
        solveRecursive(board, newRemainingPieces, row, col + 1);
    }

    private static boolean pieceFits(Piece[][] board, Piece piece, int row, int col) {
        if (!isWithinLimits(row, col) || isPositionOccupied(board, row, col)) {
            return false;
        }

        int [] sides = piece.getSides();
    
        return fitsAbove(board, sides[Orientations.UP.getValue()], row, col) &&
               fitsRight(board, sides[Orientations.RIGHT.getValue()], row, col) &&
               fitsBelow(board, sides[Orientations.DOWN.getValue()], row, col) &&
               fitsLeft(board, sides[Orientations.LEFT.getValue()], row, col);
    }

    private static boolean isWithinLimits(int row, int col) {
        return !(row < 0 || col < 0 || row >= height || col >= width);
    }
    
    private static boolean isPositionOccupied(Piece[][] board, int row, int col) {
        return board[row][col] != null;
    }

    private static boolean fitsAbove(Piece[][] board, int side, int row, int col) {
        if (row > 0) {
            return board[row - 1][col] == null || //Piece above not null and with same side
                board[row - 1][col].getSide(Orientations.DOWN.getValue()) == side;
        } else { // row == 0
            return side == BORDER;
        }
    }

    private static boolean fitsRight(Piece[][] board, int side, int row, int col) {
        if (col < (width - 1)) {
            return board[row][col + 1] == null || 
                   board[row][col + 1].getSide(Orientations.LEFT.getValue()) == side;
        } else { // col == width - 1
            return side == BORDER;
        }
    }
    
    private static boolean fitsBelow(Piece[][] board, int side, int row, int col) {
        if (row < (height - 1)) {
            return board[row + 1][col] == null || 
                   board[row + 1][col].getSide(Orientations.UP.getValue()) == side;
        } else { // row == height - 1
            return side == BORDER;
        }
    }
    
    private static boolean fitsLeft(Piece[][] board, int side, int row, int col) {
        if (col > 0) {
            return board[row][col - 1] == null || 
                   board[row][col - 1].getSide(Orientations.RIGHT.getValue()) == side;
        } else { // col == 0
            return side == BORDER;
        }
    }

    private static void placePiece(Piece[][] board, Piece piece, int row, int col) {
        board[row][col] = piece;
    }
    
    private static void removePiece(Piece[][] board, int row, int col) {
        board[row][col] = null;
    }

    private static void displaySolutions() {
        for (int[][] solution : solutions) {
            for (int[] row : solution) {
                String rowStr = Arrays.stream(row)
                                      .mapToObj(Integer::toString)
                                      .collect(Collectors.joining(" "));
                System.out.println(rowStr);
            }
            System.out.println(); // blank line between solutions
        }
    }
}