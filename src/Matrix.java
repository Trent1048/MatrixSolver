import java.util.Arrays;

public class Matrix {

    private final int ROWS;
    private final int COLS;

    private double[][] data;

    // for storing the toString value and only recalculating it when necessary
    private String asString;
    private boolean dataChanged;

    public Matrix(int ROWS, int COLS) {

        this.ROWS = ROWS;
        this.COLS = COLS;

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

    // MUTATORS

    // sets value at specified location in the matrix
    public void set(int row, int col, double value) {

        if (!validRow(row) || !validCol(col)) {
            throw new IllegalArgumentException("Inputted location out of bounds");
        }

        dataChanged = true;
        data[row][col] = value;

    }

    // sets an entire row in the matrix
    public void setRow(int row, double[] values) {

        if (values.length != COLS) {
            throw new IllegalArgumentException("The inputted values do not match the length of the matrix");
        } else if (!validRow(row)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        dataChanged = true;
        data[row] = Arrays.copyOf(values, ROWS);

    }

    // swaps the location of 2 rows
    public void swapRows(int row1, int row2) {

        if (!validRow(row1) || !validRow(row2)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        dataChanged = true;
        double[] temp = data[row1];
        data[row1] = data[row2];
        data[row2] = temp;

    }

    // multiplies an entire row by a scalar
    public void scaleRow(int row, double scalar) {
        if (!validRow(row)) {
            throw new IllegalArgumentException("Inputted row out of bounds");
        }

        for (int col = 0; col < COLS; col++) {

            data[row][col] = data[row][col] * scalar;

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
        String[][] spaces = new String[ROWS][COLS];

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