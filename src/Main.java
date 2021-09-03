import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // run tests

        test();

        // make a matrix with user input and display it

        int rows;
        int cols;

        Scanner console = new Scanner(System.in);

        System.out.print("How many rows? ");
        rows = console.nextInt();
        System.out.print("How many columns? ");
        cols = console.nextInt();

        Matrix m = new Matrix(rows, cols);

        String line;
        Scanner lineScanner;
        console.nextLine(); // clear empty line from scanner
        System.out.println("Input each value in a row by typing each number separated by spaces:");

        for (int i = 0; i < rows; i++) {

            System.out.print("Row " + (i + 1) + ": ");
            line = console.nextLine();
            lineScanner = new Scanner(line);

            for (int j = 0; j < cols; j++) {

                double value = lineScanner.nextDouble();

                m.set(i, j, value);

            }

            lineScanner.close();
        }

        System.out.println(m);
    }

    public static void test() {

        Matrix m = new Matrix(3, 3);

        double[] row1 = {1, 4.2, 98.32};
        double[] row2 = {0, 5.93212466, 7};
        double[] row3 = {54, 1.3, 0.48};

        m.setRow(0, row1);
        m.setRow(1, row2);
        m.setRow(2, row3);

        System.out.println("test matrix:");
        System.out.println(m);

        m.set(0, 1, 5);
        m.set(1, 2, 7.92);

        double[] newRow3 = {9, 81.99382, 3.8};

        m.setRow(2, newRow3);

        System.out.println("test matrix:");
        System.out.println(m);

        try {
            m.set(-1, 1, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.toString());
        }

        try {
            m.setRow(7, newRow3);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.toString());
        }

        double[] tooLongRow = {0, 43, 7.88, 67.2};

        try {
            m.setRow(1, tooLongRow);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.toString());
        }

        System.out.println();

    }

}
