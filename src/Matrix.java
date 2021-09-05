import java.util.Arrays;
import java.util.HashMap;

public class Matrix {

    private final int ROWS;
    private final int COLS;

    public static final double PRECISION = 0.00001;

    private double[][] data;

    // for storing the toString value and only recalculating it when necessary
    private String asString;
    private boolean dataChanged;

    public Matrix(int rows, int cols) {

        this.ROWS = rows;
        this.COLS = cols;

        data = new double[ROWS][COLS];

        dataChanged = true;

    }

    // ACCESSORS

    // returns value at specified location in the matrix
    public double get(int row, int col) {

        if (!validRow(row) || !validCol(col)) {
            throw new IllegalArgumentException("Inputted location out of bounds");
        }

        return data[row][col];

    }

    // returns what column the leading coefficient is in of that row
    // returns -1 if there is no leading coefficient (all 0 row)
    public int getPivotCol(int row) {
        if (!validRow(row)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        for (int col = 0; col < COLS; col++) {

            if (data[row][col] != 0) return col;

        }

        return -1;

    }

    // MUTATORS

    // sets value at specified location in the matrix
    public void set(int row, int col, double value) {

        if (!validRow(row) || !validCol(col)) {
            throw new IllegalArgumentException("Inputted location out of bounds");
        }

        data[row][col] = value;

        dataChanged = true;

    }

    // sets an entire row in the matrix
    public void setRow(int row, double[] values) {

        if (values.length != COLS) {
            throw new IllegalArgumentException("The inputted values do not match the length of the matrix");
        } else if (!validRow(row)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        data[row] = Arrays.copyOf(values, COLS);

        dataChanged = true;

    }

    // swaps the location of 2 rows
    public void swapRows(int row1, int row2) {

        if (!validRow(row1) || !validRow(row2)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        double[] temp = data[row1];
        data[row1] = data[row2];
        data[row2] = temp;

        dataChanged = true;

    }

    // sorts the rows so that all zero rows are on the bottom and leading entries either go down or down and right
    public void sortRows() {

        // map each row to how many leading 0s it has so as not to
        // recalculate every time, which takes a lot of computation

        HashMap<double[], Integer> leadingZeros = new HashMap<>();
        double[] rowArr;
        int leadingZerosForRow;

        for (int row = 0; row < ROWS; row++) {

            rowArr = data[row];
            leadingZerosForRow = getPivotCol(row);
            if (leadingZerosForRow == -1) { // full zero row
                leadingZerosForRow = COLS;
            }

            leadingZeros.put(rowArr, leadingZerosForRow);

        }

        // use an insertion sort algorithm to sort them by least
        // to greatest number of 0s before the leading coefficient

        double[] lowerRow;
        double[] upperRow;

        // go through each row top to bottom
        for (int row = 1; row < ROWS; row++) {

            // loop through every row bottom to top, starting at the current one
            // and moves the row with least leading zeros towards the top
            for (int i = row; i > 0; i--) {

                lowerRow = data[i];
                upperRow = data[i - 1];

                if (leadingZeros.get(lowerRow) < leadingZeros.get(upperRow)) {
                    // swap rows
                    data[i] = upperRow;
                    data[i - 1] = lowerRow;
                } else {
                    break;
                }
            }
        }

        dataChanged = true;
    }

    // multiplies an entire row by a scalar
    public void scaleRow(int row, double scalar) {
        if (!validRow(row)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        for (int col = 0; col < COLS; col++) {

            data[row][col] = data[row][col] * scalar;

            // to eliminate floating point errors
            if (Math.abs(data[row][col]) < PRECISION) {
                data[row][col] = 0;
            }

        }

        dataChanged = true;
    }

    // scales the inputted row so that the leading coefficient = 1
    public void normalizeRow(int row) {
        if (!validRow(row)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        int pivotCol = getPivotCol(row);
        if (pivotCol != -1) { // don't need to do anything if the row is all 0s

            double pivotValue = get(row, pivotCol);
            if (pivotValue != 1) { // don't need to do anything if the leading coefficient is already 1

                scaleRow(row, 1/pivotValue);

            }
        }

        dataChanged = true;
    }

    // adds row1 times scalar to row2
    public void addRows(int row1, int row2, double scalar) {
        if (!validRow(row1) || !validRow(row2)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        for (int col = 0; col < COLS; col++) {

            data[row2][col] += data[row1][col] * scalar;

            // to eliminate floating point errors
            if (Math.abs(data[row2][col]) < PRECISION) {
                data[row2][col] = 0;
            }

        }

        dataChanged = true;
    }

    // adds row1 to row2 a number of times so row2 has a value of 0 in the pivot column of row1
    public void zeroOtherRowInPivotColumn(int row1, int row2) {
        if (!validRow(row1) || !validRow(row2)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        int pivotCol = getPivotCol(row1);
        if (pivotCol != -1) { // don't need to do anything if the row is all 0s

            double otherRowValInPivotCol = get(row2, pivotCol);
            if (otherRowValInPivotCol != 0) { // don't need to do anything if the value in the pivot column is already 0

                double pivotValue = get(row1, pivotCol);
                addRows(row1, row2, -otherRowValInPivotCol/pivotValue);

                // gets rid of those pesky values like 2 * 10^-14 that
                // should be zero, but aren't due to floating point error
                data[row2][pivotCol] = 0;

            }
        }

        dataChanged = true;
    }

    // makes all 0s in rows below the inputted row within that row's pivot column
    public void zeroPivotColsBelow(int startingRow) {
        if (!validRow(startingRow)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        // loop through rows below and zero them
        for (int row = startingRow + 1; row < ROWS; row++) {

            zeroOtherRowInPivotColumn(startingRow, row);

        }

        dataChanged = true;

    }

    // makes all 0s in rows above the inputted row within that row's pivot column
    public void zeroPivotColsAbove(int startingRow) {
        if (!validRow(startingRow)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        // loop through rows above and zero them
        for (int row = startingRow - 1; row >= 0; row--) {

            zeroOtherRowInPivotColumn(startingRow, row);

        }

        dataChanged = true;

    }

    // TODO: add detection for no solution

    // puts the matrix into echelon form
    public void echelonForm() {
        sortRows();
        for (int row = 0; row < ROWS - 1; row++) {
            zeroPivotColsBelow(row);
        }
        dataChanged = true;
    }

    // puts the matrix into reduced echelon form
    public void reducedEchelonForm() {
        echelonForm();
        for (int row = ROWS - 1; row > 0; row--) {
            zeroPivotColsAbove(row);
            normalizeRow(row);
        }
        dataChanged = true;
    }

    // HELPER FUNCTIONS

    // checks whether an inputted row value is within the bounds of the matrix
    private boolean validRow(int row) {

        return row >= 0 && row < ROWS;

    }

    // checks whether an inputted column value is within the bounds of the matrix
    private boolean validCol(int col) {

        return col >= 0 && col < COLS;

    }

    // TO_STRING RELATED FUNCTIONS

    public String toString() {

        // only goes through the expensive String recalculation process when changes have occurred to the data
        if (!dataChanged) return asString;
        dataChanged = false;

        // get display values for every value in the matrix and spaces associated with them
        String[][] displayValues = new String[ROWS][COLS];
        String[][] spaces;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                displayValues[row][col] = displayValue(data[row][col]);
            }
        }
        spaces = getSpaces(displayValues);

        // put everything together
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            builder.append("|  "); // left edge of matrix
            for (int col = 0; col < COLS; col++) {
                builder.append(displayValues[row][col]);
                builder.append(spaces[row][col]);
            }
            builder.append("|\n"); // right edge of matrix
        }

        asString = builder.toString();
        return asString;
    }

    // returns a nice looking value for display purposes
    // removes decimal from any integer
    // leaves only 2 decimal places for non-integers
    private String displayValue(double value) {
        String displayVal = "" + value;
        String[] parts = displayVal.split("\\.");

        // integer
        if (parts[1].equals("0")) {
            return parts[0];
        }

        // 2 digit decimal
        if (parts[1].length() > 2) {
            return parts[0] + "." + parts[1].substring(0, 2);
        }

        // less than 2 digit decimal
        return displayVal;
    }

    // takes in 2d string array of display values for the matrix
    // returns a 2d array containing the spaces that should follow each value
    // to make each subsequent column start at the same spot horizontally
    private String[][] getSpaces(String[][] displayValues) {
        String[][] spaces = new String[ROWS][COLS];
        final int EXTRA_SPACES = 2; // minimum amount of spaces to follow each number

        for (int col = 0; col < COLS; col++) {
            // find longest value in string form
            int longestValueLen = 0;
            for (int row = 0; row < ROWS; row++) {
                int valueLen = displayValues[row][col].length();
                if (valueLen > longestValueLen) {
                    longestValueLen = valueLen;
                }
            }
            // put a corresponding amount of spaces in the spaces array
            // so they all will match up at the start of the next column
            for (int row = 0; row < ROWS; row++) {
                int valueLen = displayValues[row][col].length();
                // match the length of the longest value + specified number of extra spaces
                int numOfSpaces = longestValueLen - valueLen + EXTRA_SPACES;

                StringBuilder spacesBuilder = new StringBuilder();
                for (int i = 0; i < numOfSpaces; i++) {
                    spacesBuilder.append(" ");
                }
                spaces[row][col] = spacesBuilder.toString();
            }
        }

        return spaces;
    }
}