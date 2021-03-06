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

        if (rows < 0 || cols < 0) throw new IllegalArgumentException("The matrix cannot have a dimension of 0");

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

    // sorts the rows so that all zero rows are on the bottom and leading entries go down and right
    public void sortRows() {
        sortRows(1);
    }

    // sorts the rows so the zero rows are on the bottom and leading entries go down and right
    // starts the sorting process at startingRow, useful for when the top part is known to be sorted
    // but row operations may cause lower potions to no longer be sorted
    private void sortRows(int startingRow) {

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
        int lowerLeadingZeroes;

        // go through each row top to bottom, skipping the first one because that is
        // already assumed to be in the "sorted" section of the matrix
        for (int row = startingRow; row < ROWS; row++) {

            // get row we are trying to insert into sorted upper
            // portion and calculate how many leading zeroes it has
            lowerRow = data[row];
            lowerLeadingZeroes = leadingZeros.get(lowerRow);

            // loop through every row bottom to top, starting at the current one
            // and continue to move that row up as long as it has less leading zeroes
            // than the one above it
            for (int i = row; i > 0; i--) {

                // row currently directly above the current row we are trying to place
                upperRow = data[i - 1];

                if (lowerLeadingZeroes < leadingZeros.get(upperRow)) {
                    // swap rows
                    data[i] = upperRow;
                    data[i - 1] = lowerRow;

                    dataChanged = true;
                } else {
                    break;
                }
            }
        }
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
    public void zeroBelowPivotCols(int startingRow) {
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
    public void zeroAbovePivotCols(int startingRow) {
        if (!validRow(startingRow)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        // loop through rows above and zero them
        for (int row = startingRow - 1; row >= 0; row--) {

            zeroOtherRowInPivotColumn(startingRow, row);

        }

        dataChanged = true;

    }

    // puts the matrix into echelon form
    public void echelonForm() {
        sortRows();
        for (int row = 0; row < ROWS - 1; row++) {
            zeroBelowPivotCols(row);
            sortRows(row + 1); // to make sure matrix rows become unsorted after row operations
        }
        dataChanged = true;
    }

    // puts the matrix into reduced echelon form
    public void reducedEchelonForm() {
        echelonForm();
        normalizeRow(0);
        for (int row = ROWS - 1; row > 0; row--) {
            zeroAbovePivotCols(row);
            normalizeRow(row);
        }
        dataChanged = true;
    }

    // calculates the determinant of the matrix by calling the recursive function
    public double determinant() {

        if (ROWS != COLS) {
            throw new IllegalArgumentException("Matrix must be square to find the determinant");
        }

        // base case: m is a 2 x 2 matrix
        // (don't need to check column because we already know it is square)
        if (ROWS == 2) {
            // | a b |
            // | c d |
            // a 2 x 2 matrix as shown has a determinant ad - bc
            return get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0);
        }

        // variables for upcoming loop:
        int plusMinus = 1;
        double sum = 0.0;
        int row = 0;
        Matrix excludedRowColMatrix;
        double excludedRowColDeterminant;

        // loop through each value in the first row
        // multiply by that value times the determinant
        // and + - + - them up together
        for (int col = 0; col < COLS; col++) {
            excludedRowColMatrix = getMatrixWithExcludedRowAndCol(row, col);
            excludedRowColDeterminant = excludedRowColMatrix.determinant();
            sum += get(row, col) * excludedRowColDeterminant * plusMinus;
            plusMinus *= -1; // flip value of plusMinus for next iteration
        }

        return sum;
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

    // for use with the determinant function
    // returns a new matrix that contains values of this
    // matrix without any from the specified row and column
    private Matrix getMatrixWithExcludedRowAndCol(int excludedRow, int excludedCol) {
        int newRows = ROWS - 1;
        int newCols = COLS - 1;

        if (newRows < 1 || newCols < 1) {
            throw new IllegalArgumentException("Cannot make a new matrix with dimension of 0");
        }

        Matrix m = new Matrix(newRows, newCols);

        // add either 0 or 1 to the row/column after excluded row/column has been hit
        // to get values to line up correctly in the new matrix
        int additionalRow = 0;
        int additionalCol = 0;

        for (int row = 0; row < ROWS; row++) {
            // skip the excluded row and update the additionalRow variable
            // to account for the excluded row when indexing the matrix
            if (row == excludedRow) {
                additionalRow = 1;
                continue;
            }
            // reset every row because columns before the skipped one don't need to add 1 to the index
            additionalCol = 0;
            for (int col = 0; col < COLS; col++) {
                // skip the excluded column and update the additionalCol variable
                // to account for the excluded column when indexing the matrix
                if (col == excludedCol) {
                    additionalCol = 1;
                    continue;
                }
                m.set(row - additionalRow, col - additionalCol, get(row, col));
            }
        }

        return m;
    }

    // TO_STRING RELATED FUNCTIONS

    public String toString() {
        return toString(false);
    }

    public String toString(boolean augmented) {

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
            for (int col = 0; col < COLS - 1; col++) {
                builder.append(displayValues[row][col]);
                builder.append(spaces[row][col]);
            }
            if (COLS > 1 && augmented) { // don't show the augmented matrix bar if there is only one column
                builder.append("|  ");
            }
            builder.append(displayValues[row][COLS - 1]);
            builder.append(spaces[row][COLS - 1]);
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