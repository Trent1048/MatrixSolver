import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int rows;
        int cols;

        Scanner console = new Scanner(System.in);

        System.out.print("How many rows? ");
        rows = console.nextInt();
        System.out.print("How many columns? ");
        cols = console.nextInt();

        int[][] matrix = new int[rows][cols];

        String line;
        Scanner lineScanner;
        console.nextLine(); // clear empty line from scanner
        System.out.println("Input each value in a row by typing each number separated by spaces:");

        for (int i = 0; i < rows; i++) {

            System.out.print("Row " + i + ": ");
            line = console.nextLine();
            lineScanner = new Scanner(line);

            for (int j = 0; j < cols; j++) {

                matrix[i][j] = lineScanner.nextInt();

            }

            lineScanner.close();
        }

        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

}
