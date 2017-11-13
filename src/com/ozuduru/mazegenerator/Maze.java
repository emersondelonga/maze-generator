import static java.lang.System.*;
import java.util.Scanner;
import java.io.File;
import java.lang.Exception;
public class Maze
{
    private Cell[][] board;
    private final int DELAY = 1;
    private int prevRow = 0;
    private int prevCol = 0;

    public Maze(int rows, int cols, int[][] map){
        StdDraw.setXscale(0, cols);
        StdDraw.setYscale(0, rows);
        board = new Cell[rows][cols];
        //grab number of rows to invert grid system with StdDraw (lower-left, instead of top-left)
        int height = board.length - 1;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                board[r][c] = map[r][c] == 1 ? new Cell(c , height - r, 0.5, false) : new Cell(c, height - r, 0.5, true);
            }
    }

    public void draw()
    {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[r].length; c++){
                Cell cell = board[r][c];
                StdDraw.setPenColor(cell.getColor());
                StdDraw.filledSquare(cell.getX(), cell.getY(), cell.getRadius());
            }
            StdDraw.show();
    }

    public boolean findPath(int row, int col) {
        boolean isFinished = false;

        if (isValid(row, col)) {
            board[row][col].visitCell();
            board[prevRow][prevCol].setColor(StdDraw.RED);
            board[row][col].setColor(StdDraw.BLUE);
            prevRow = row;
            prevCol = col;

            this.draw();
            StdDraw.pause(DELAY);

            if(isExit(row, col))
            {
                board[row][col].becomePath();
                isFinished = true;
            }

            if (!isFinished) {
                isFinished = findPath(row + 1, col);
                if (!isFinished)
                    isFinished = findPath(row, col + 1);
                if (!isFinished)
                    isFinished = findPath(row, col - 1);
                if (!isFinished)
                    isFinished = findPath(row - 1, col);
            }
        }
        if(isFinished)
        {
            board[row][col].becomePath();
        }
            return isFinished;
    }

    private boolean isValid(int row, int col)
    {
        //ensure that (row, col) is a valid location in grid
        if (row >= 0 && row < board.length && col >= 0 && col < board[row].length) {
            if (board[row][col].isWall() || board[row][col].isVisited())
                return false;
            else
                return true;
        }
        return false;
    }

    private boolean isExit(int row, int col)
    {
        if (row == board.length - 1 && col == board[row].length - 1)
            return true;
        else
            return false;
    }
    public static int[][] readMaze() throws Exception {
        int size = 10;
        String[][] dataSet = new String[size][size];
        int[][] numSet = new int[size][size];
        String fileName = "Workbook1";
        String extension = ".csv";
        File inputFile = new File(fileName + extension);
        Scanner inputObject = new Scanner(inputFile);
        //inputObject.useDelimiter(",");
        //System.out.println("delim is:" + inputObject.delimiter());
        int row = 0;
        // process the entire file, with a loop
        // populate your data structures
        while (inputObject.hasNextLine() == true) {
            dataSet[row] = (inputObject.nextLine()).split(",");
            row++;
        }
        for (int r = 0; r < dataSet.length; r++) {
            for (int c = 0; c < dataSet[0].length; c++) {
                dataSet[r][c] = dataSet[r][c].trim();
                System.out.println(dataSet[r][c]);
                numSet[r][c] = Integer.parseInt(dataSet[r][c].trim());
            }
            System.out.println();
        }
        inputObject.close();
        return numSet;

    }
    public static int[][] generateMaze(int size, double prob) {
        int maze[][] = new int[size][size];
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                maze[i][j] = 1;
            }
        }
        for (int i = 2; i < size - 1; i += 2) {
            for (int j = 1; j < size - 1; j++) {
                maze[i][j] = 0;
            }
        }
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 2; j += 2) {
                maze[i][j] = 0;
            }
        }
        for (int i = 2; i < size - 1; i+= 2) {
            for (int j = 1; j < size - 1; j+=1) {
                if(maze[i][j] == 0)
                {
                    if((int)(Math.random()*prob) > 0)
                    maze[i][j] = 1;
                }
            }
        }
        maze[0][0] = 1;
        maze[0][1] = 1;
        maze[0][2] = 1;
        maze[0][3] = 1;
        maze[1][0] = 1;
        maze[1][1] = 1;
        maze[1][2] = 1;
        maze[1][3] = 1;
        maze[size - 1][size - 1] = 1;
        maze[size - 1][size - 2] = 1;
        maze[size - 2][size - 1] = 1;
        maze[size - 2][size - 2] = 1;
        maze[size - 3][size - 1] = 1;
        maze[size - 3][size - 2] = 1;
        maze[size - 4][size - 1] = 1;
        maze[size - 4][size - 2] = 1;
        return maze;
    }

    public static void main(String[] args) throws Exception{
        StdDraw.enableDoubleBuffering();
        int[][] maze = generateMaze(100,3.5);
        Maze geerid = new Maze(maze.length, maze[0].length, maze);
        geerid.draw();
        geerid.findPath(0, 0);
        geerid.draw();
    }
}
