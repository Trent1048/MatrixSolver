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

        double[] row1 = {1, 4.2, 9.32};
        double[] row2 = {0, -5.936, 7};
        double[] row3 = {19, 95, 0.48};

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

        m.scaleRow(2, 1.0/9);
        m.swapRows(1, 2);

        System.out.println("test matrix");
        System.out.println(m);

        m.addRows(0, 1, -1);

        System.out.println("test matrix");
        System.out.println(m);

        System.out.println("leading coefficient of row 1 in column: " + m.getPivotCol(1) + "\n");

        double[] zeros = {0, 0, 0};

        m.setRow(2, zeros);

        System.out.println("test matrix");
        System.out.println(m);

        System.out.println("leading coefficient of row 2 in column: " + m.getPivotCol(2) + "\n");

        m.normalizeRow(1);

        System.out.println("test matrix");
        System.out.println(m);

        m.zeroOtherRowInPivotColumn(1, 0);

        System.out.println("test matrix");
        System.out.println(m);

        Matrix mSorted = new Matrix(6, 5);

        double[] noZeros = {1, 2, 3, 4, 5};
        double[] oneZero = {0, 1, 2, 3, 4};
        double[] twoZeros = {0, 0, 1, 2, 3};
        double[] threeZeros = {0, 0, 0, 1, 2};
        double[] fourZeros = {0, 0, 0, 0, 1};
        double[] fiveZeros = {0, 0, 0, 0, 0};

        mSorted.setRow(0, twoZeros);
        mSorted.setRow(1, fiveZeros);
        mSorted.setRow(2, noZeros);
        mSorted.setRow(3, threeZeros);
        mSorted.setRow(4, fourZeros);
        mSorted.setRow(5, oneZero);

        System.out.println("test matrix");
        System.out.println(mSorted);

        mSorted.sortRows();

        System.out.println("test matrix");
        System.out.println(mSorted);

        m = new Matrix(3, 4);

        double[] row4 = {1, 4.2, 9.4, 3.12};
        double[] row5 = {0, -5.936, 7, 52};
        double[] row6 = {19, 1.3, 0.48, 0};

        m.setRow(0, row4);
        m.setRow(1, row5);
        m.setRow(2, row6);

        System.out.println("test matrix");
        System.out.println(m);

        m.zeroPivotColsBelow(0);

        System.out.println("test matrix");
        System.out.println(m);

        m.zeroPivotColsBelow(1);

        System.out.println("test matrix");
        System.out.println(m);

        m.normalizeRow(1);

        System.out.println("test matrix");
        System.out.println(m);

        m.setRow(0, row4);
        m.setRow(1, row5);
        m.setRow(2, row6);

        System.out.println("test matrix");
        System.out.println(m);

        m.echelonForm();

        System.out.println("test matrix (echelon form)");
        System.out.println(m);

        m.reducedEchelonForm();

        System.out.println("test matrix (reduced echelon form)");
        System.out.println(m);

        m.setRow(0, row4);
        m.setRow(1, row5);
        m.setRow(2, row6);

        System.out.println("test matrix");
        System.out.println(m);

        m.reducedEchelonForm();

        System.out.println("test matrix (reduced echelon from fresh)");
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

        try {
            m.swapRows(-1, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.toString());
        }

        System.out.println();

    }

}
