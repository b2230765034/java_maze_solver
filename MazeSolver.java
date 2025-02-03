// This assignment was pretty challenging for me. Nearly I studied everday to finish it and this is the best I can do.
// But I learned so many different methods and now I am better with debugging than I was before.

package javaAssignments.A1;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MazeSolver {

    // Method to read the maze grid from a text file
    public static char[][] readMazeGrid(String input) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        int rows = 0;

        // Find out the number of rows in the maze
        while((line = reader.readLine()) != null) {
            rows++;
        }

        reader.close();
        reader = new BufferedReader(new FileReader(input));

        char [][] mazeGrid = new char[rows][];

        // Read the file again and fill the maze grid with characters
        int row = 0;
        while((line = reader.readLine()) != null) {
            mazeGrid[row] = line.toCharArray(); // Change the string into a char array
            row++;
        }
        reader.close();
        return mazeGrid; // Return the maze grid
    }

    // Method to add walls around the maze to avoid boundary issues
    public static char[][] addWalls(char [][] mazeGrid)  {
        Integer rowNum = mazeGrid.length; // Number of rows in the original maze
        Integer colNum = mazeGrid[0].length; // Number of columns in the original maze

        // Create a new maze grid with extra rows and columns for walls
        char[][] newMazeGrid = new char[rowNum+2][colNum+2];

        // Add walls to new maze grid (top, bottom, right and left side)
        for (int i = 0; i < newMazeGrid.length; i++) {
            int a = (newMazeGrid[0].length) - 1;
            newMazeGrid[i][0] = '#'; // Left wall
            newMazeGrid[i][a] = '#'; // Right wall
        }
        for (int j = 0; j < newMazeGrid[0].length; j++) {
            int b = (newMazeGrid.length) - 1;
            newMazeGrid[0][j] = '#'; // Top wall
            newMazeGrid[b][j] = '#'; // Bottom wall
        }

        // Add the original maze inside the new grid, surrounded by walls
        for (int i = 0; i < mazeGrid.length; i++) {
            for (int j = 0; j < mazeGrid[0].length; j++) {
                newMazeGrid[i+1][j+1] = mazeGrid[i][j] ;
            }
        }
        // Return the new maze grid with walls
        return newMazeGrid;
    }

    // Method to print the maze
    public static void printMaze(char [][] mazeGrid){
        for(int i = 0; i < mazeGrid.length; i++){ // Row part
            for(int j = 0; j < mazeGrid[i].length; j++){ // Col part
                System.out.print(mazeGrid[i][j] + " ");
            }
            // Move to the next line after printing a row
            System.out.println();
        }
    }

    // Maze solving method using a simple left-hand-rule kinda approach
    public static char[][] mazeSolver(char [][] newMazeGrid, int startX, int startY) {
        // Start point in the maze
        Integer curr_X = startX + 1; // X: row number , adding one to match input to new wall maze
        Integer curr_Y = startY + 1; // Y: col number , adding one to match input to new wall maze
        int[][] visited = new int[newMazeGrid.length][newMazeGrid[0].length]; // Create an array for visited cells
        int[][] visitedBack = new int[newMazeGrid.length][newMazeGrid[0].length]; // Array for visited backtrack cells
        Integer direction = 1; // Start facing East;  0: South(g), 1: East(d), 2: North(k), 3: West(b)
        Integer startPointX = startX + 1, startPointY = startY + 1; // Current position

        visited[startPointX][startPointY] = 1; // Mark the start point as visited cell

        // Main maze solving logic
        while (true) {

            boolean flag_moved = false; // Flag to check if the algorithm moved

            // Move East if not wall and not visited
            if ((newMazeGrid[curr_X][curr_Y + 1] != '#') && (visited[curr_X][curr_Y + 1] != 1)) {
                curr_Y++;
                visited[curr_X][curr_Y] = 1; // Mark current cell as visited
                direction = 1; // Update direction to North to turn left
                flag_moved = true;

                // Keep moving straight unless its in exit or not wall ahead
                while ((newMazeGrid[curr_X][curr_Y + 1] != '#') && (newMazeGrid[curr_X][curr_Y] != 'X')) {
                    curr_Y++;
                    visited[curr_X][curr_Y] = 1;
                }

                // Move North if not wall and not visited
            } else if ((newMazeGrid[curr_X - 1][curr_Y] != '#') && (visited[curr_X - 1][curr_Y] != 1)) {
                curr_X--;
                visited[curr_X][curr_Y] = 1; // Mark current cell as visited
                direction = 2; // Update direction to West to turn left
                flag_moved = true;

                // Keep moving straight unless its in exit or not wall ahead
                while ((newMazeGrid[curr_X - 1][curr_Y] != '#') && (newMazeGrid[curr_X][curr_Y] != 'X')) {
                    curr_X--;
                    visited[curr_X][curr_Y] = 1;
                }

                // Move West if not wall and not visited
            } else if ((newMazeGrid[curr_X][curr_Y - 1] != '#') && (visited[curr_X][curr_Y - 1] != 1)) {
                curr_Y--;
                visited[curr_X][curr_Y] = 1; // Mark current cell as visited
                direction = 3; // Update direction to South to turn left
                flag_moved = true;

                // Keep moving straight unless its in exit or not wall ahead
                while ((newMazeGrid[curr_X][curr_Y - 1] != '#') && (newMazeGrid[curr_X][curr_Y] != 'X')) {
                    curr_Y--;
                    visited[curr_X][curr_Y] = 1;
                }

                // Move South if not wall and not visited
            } else if ((newMazeGrid[curr_X + 1][curr_Y] != '#') && (visited[curr_X + 1][curr_Y] != 1)) {
                curr_X++;
                visited[curr_X][curr_Y] = 1; // Mark current cell as visited
                direction = 0; // Update direction to East to turn left
                flag_moved = true;

                // Keep moving straight unless its in exit or not wall ahead
                while ((newMazeGrid[curr_X + 1][curr_Y] != '#') && (newMazeGrid[curr_X][curr_Y] != 'X')) {
                    curr_X++;
                    visited[curr_X][curr_Y] = 1;
                }
            }

            // Backtracking
            if (!flag_moved) { // Backtrack while not unvisited cell

                visitedBack[curr_X][curr_Y] = 1; // Mark the start point as visited cell in Backtrack visited array
                direction = (direction + 1) % 4; // Turn left after each loop

                // Move forward if; direction is correct, no wall ahead, not revisited
                if ((direction == 3) && (newMazeGrid[curr_X][curr_Y - 1] != '#') && (visitedBack[curr_X][curr_Y - 1] != 1)) {
                    curr_Y--;
                    visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited

                    // Keep moving forward while; no wall ahead, visited and no way on the left
                    while ((newMazeGrid[curr_X][curr_Y - 1] != '#') && (visited[curr_X][curr_Y] == 1) &&
                            (newMazeGrid[curr_X + 1][curr_Y] != '.')) {
                        curr_Y--;
                        visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited
                    }

                    // Move forward if; direction is correct, no wall ahead, not revisited
                } else if ((direction == 0) && (newMazeGrid[curr_X + 1][curr_Y] != '#') && (visitedBack[curr_X + 1][curr_Y] != 1)) {
                    curr_X++;
                    visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited

                    // Keep moving forward while; no wall ahead, visited and no way on the left
                    while ((newMazeGrid[curr_X + 1][curr_Y] != '#') && (visited[curr_X][curr_Y] == 1) &&
                            (newMazeGrid[curr_X][curr_Y + 1] != '.')) {
                        curr_X++;
                        visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited
                    }

                    // Move forward if; direction is correct, no wall ahead, not revisited
                } else if ((direction == 1) && (newMazeGrid[curr_X][curr_Y + 1] != '#') && (visitedBack[curr_X][curr_Y + 1] != 1)) {
                    curr_Y++;
                    visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited

                    // Keep moving forward while; no wall ahead, visited and no way on the left
                    while ((newMazeGrid[curr_X][curr_Y + 1] != '#') && (visited[curr_X][curr_Y] == 1) &&
                            (newMazeGrid[curr_X - 1][curr_Y] != '.')) {
                        curr_Y++;
                        visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited
                    }

                    // Move forward if; direction is correct, no wall ahead, not revisited
                } else if ((direction == 2) && (newMazeGrid[curr_X - 1][curr_Y] != '#') && (visitedBack[curr_X - 1][curr_Y] != 1)) {
                    curr_X--;
                    visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited

                    // Keep moving forward while; no wall ahead, visited and no way on the left
                    while ((newMazeGrid[curr_X - 1][curr_Y] != '#') && (visited[curr_X][curr_Y] == 1) &&
                            (newMazeGrid[curr_X][curr_Y - 1] != '.')) {
                        curr_X--;
                        visitedBack[curr_X][curr_Y] = 1; // Mark current cell as revisited
                    }

                }
            }

            // Check if we've reached the exit ('X')
            if (newMazeGrid[curr_X][curr_Y] == 'X') {
                System.out.println("You found the exit! ");
                System.out.println("Exit location: " + newMazeGrid[curr_X][curr_Y] + " x:" + curr_X + ",y:" + curr_Y);
                break;
            } else if (curr_X == startPointX && curr_Y == startPointY) {
                System.out.println("There is no solution!");
                break;
            }

        }

        return newMazeGrid; // Returns maze grid with walls around it
    }


    public static void main(String[] args) throws IOException {

        // Check correct input argument length
        if (args.length != 3 ) {
            System.out.println("Usage: java MazeSolver <x-coordinate> <y-coordinate> <maze-file>");
            return;
        }

        try {
            int startX = Integer.parseInt(args[0]); // Set 0. arg to X start location
            int startY = Integer.parseInt(args[1]); // Set 1. arg to Y start location
            String mazeFile = args[2]; // Set 2. arg to string mazeFile as file name

            char[][] mazeGrid = MazeSolver.readMazeGrid(mazeFile); // Read the txt file
            char[][] newMazeGrid = MazeSolver.addWalls(mazeGrid); // Add walls around it
            mazeSolver(newMazeGrid, startX, startY); // Call maze solver function

        } catch (NumberFormatException e) { // Check the first two arg to be integers
            System.out.println("Error: Coordinates must be integers.");

        } catch (FileNotFoundException e) { // Check if the file exist or not inside the directory
            System.out.println("Error: Maze file not found.");
        }
    }
}
