import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PuzzleSolver {
    private static int width, height;
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

    private static void solvePuzzle() {
        Piece[][] board = new Piece[height][width]; // Create an empty board
        
        solveRecursive(board, new ArrayList<>(pieces), 0, 0); // Start solving the puzzle
    }
        
    private static void solveRecursive(Piece[][] board, List<Piece> remainingPieces, int row, int col) {
        //System.out.println("Solving - Row: "+ row+ " col: "+col );
        
        if (row == height) { // If we have filled the board.
            System.out.println("FILLED THE BOARD");
            int[][] solution = new int[height][width];
            for (int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++)
                {
                    solution[i][j] = board[i][j].getRowIndex();
                }
            }
            solutions.add(solution); // Add the solution
            return;
        }
        
        if (col == width) { // If we have reached the end of a row
            System.out.println("END OF A ROW - Row: "+ row+ " col: "+col );

            for(int i = 0; i < row; i++){
                for(int j = 0; j < width; j++)
                {
                    System.out.println(board[i][j].getRowIndex());
                }
            }

            solveRecursive(board, remainingPieces, row + 1, 0); // Move to the next row
            return;
        }
    
        // Fill row by row
        for (int i = 0; i < remainingPieces.size(); i++) {
            Piece piece = remainingPieces.get(i);
        
            // Try all rotations of the piece
            for (int rotation = 0; rotation < 4; rotation++) {
                piece.rotate(); // Rotate the piece
        
                if (pieceFits(board, piece, row, col)) { // If the piece fits
                    placePiece(board, piece, row, col); // Place the piece on the board
        
                    // Remove the piece from the remaining pieces and continue with the next position
                    List<Piece> newRemainingPieces = new ArrayList<>(remainingPieces);
                    newRemainingPieces.remove(i);
                    solveRecursive(board, newRemainingPieces, row, col + 1);
        
                    removePiece(board, piece, row, col); // No solution - prune - remove from board
                }
            }
        }
    }

    private static boolean pieceFits(Piece[][] board, Piece piece, int row, int col) {
        // Check if the piece fits within the limits of the board
        //System.out.println("checking fit");
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return false;
        }
    
        // Check if the position on the board is already occupied
        if (board[row][col] != null) {
            return false;
        }

        // Check if the piece fits with the surrounding pieces
        // Assuming that the Piece class has a getSides() method that returns an array of integers representing the sides
        int[] sides = piece.getSides();
    
        // Check above
        if (row > 0) {
            if (board[row - 1][col] != null && 
                board[row - 1][col].getSides()[Orientations.DOWN.getValue()] 
                != sides[Orientations.UP.getValue()]) {
                return false;
            }
        } else if (sides[Orientations.UP.getValue()] != 0) { //row == 0
            return false;
        }

        // Check right
        if(col < (width - 1)){
            if(board[row][col + 1] != null && 
            board[row][col + 1].getSides()[Orientations.LEFT.getValue()] 
            != sides[Orientations.RIGHT.getValue()]){
                return false;
            }
        } else if(sides[Orientations.RIGHT.getValue()] != 0){ //col == width
            return false;
        }
        //System.out.println("AAAAAA");

        // Check below
        if(row < (height - 1)){
            if(board[row + 1][col] != null && 
            board[row + 1][col].getSides()[Orientations.UP.getValue()] 
            != sides[Orientations.DOWN.getValue()]){
                return false;
            }
        } else if(sides[Orientations.DOWN.getValue()] != 0){ //row == height
            return false;
        }

        if(col > 0){
            if(board[row][col - 1] != null && 
            board[row][col - 1].getSides()[Orientations.RIGHT.getValue()] 
            != sides[Orientations.LEFT.getValue()]){
                return false;
            }
        } else if(sides[Orientations.LEFT.getValue()] != 0){ //col == 0
            return false;
        }
    
        //System.out.print("PIECE FITS");

        // If all checks passed, the piece fits
        return true;
    }

    private static void placePiece(Piece[][] board, Piece piece, int row, int col) {
        // Asumiendo que cada pieza tiene un ID único
        System.err.println("piece PLACED - Piece: "+ piece.getRowIndex());
        board[row][col] = piece;
    }
    
    private static void removePiece(Piece[][] board, Piece piece, int row, int col) {
        // Asumiendo que un valor de 0 representa un espacio vacío en el tablero
        System.err.println("piece REMOVED - Piece: "+ piece.getRowIndex());
        board[row][col] = null;
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