import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Matrix m = null;
        int rows;
        int cols;

        Scanner console = new Scanner(System.in);
        String command;

        System.out.println("Welcome, this is a program that can put an augmented matrix into echelon or reduced echelon form\n");

        mainLoop:
        while (true) {

            System.out.println("""
                    Please enter a command:
                    \tInput an augmented matrix (I)
                    \tConvert inputted matrix into echelon form (E)
                    \tConvert inputted matrix into reduced echelon form (R)
                    \tQuit the program (Q)
                    """);

            command = console.nextLine().toUpperCase();

            switch (command) {
                case "I":

                    // get size of the matrix (rows, columns)
                    // while loops are to ensure a validly sized matrix

                    while (true) {
                        System.out.print("How many rows? ");
                        String input = console.nextLine();

                        try {
                            rows = Integer.parseInt(input);
                            if (rows > 0) {
                                break;
                            } else {
                                System.out.println("Invalid input, please chose a number larger than 1");
                            }
                        } catch (NumberFormatException e) { // if the user doesn't input an int
                            System.out.println("Invalid input, please input an integer");
                        }
                    }
                    while (true) {
                        System.out.print("How many columns? ");
                        String input = console.nextLine();

                        try {
                            cols = Integer.parseInt(input);
                            if (cols > 0) {
                                break;
                            } else {
                                System.out.println("Invalid input, please chose a number larger than 1");
                            }
                        } catch (NumberFormatException e) { // if the user doesn't input an int
                            System.out.println("Invalid input, please input an integer");
                        }
                    }

                    m = new Matrix(rows, cols);

                    String line;
                    Scanner lineScanner;

                    // get each row inputted
                    System.out.println("Input each value in a row by typing each number separated by spaces:");

                    for (int i = 0; i < rows; i++) {

                        // while loop to make sure each row is inputted correctly, or ask again until one is
                        // correctly inputted
                        while (true) {
                            System.out.print("Row " + (i + 1) + ": ");
                            line = console.nextLine();
                            lineScanner = new Scanner(line);
                            String token;
                            double value;
                            boolean inputError = false;

                            for (int j = 0; j < cols; j++) {

                                try { // sends an error message when there weren't enough entries inputted
                                    token = lineScanner.next();
                                } catch (NoSuchElementException e) {
                                    System.out.println("Invalid input, make sure to input the same amount of entries" +
                                            " as there are columns in the matrix (" + cols + ")");
                                    inputError = true;
                                    break;
                                }
                                try {
                                    value = Double.parseDouble(token);
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input, please only include numbers");
                                    inputError = true;
                                    break;
                                }

                                m.set(i, j, value);

                            }
                            if (!inputError) {
                                if (!lineScanner.hasNext()) {
                                    break;
                                } else {
                                    // sends an error message when too many entries were inputted
                                    // but only if the user didn't provide an invalid input
                                    System.out.println("Invalid input, make sure to input the same amount of entries" +
                                            " as there are columns in the matrix (" + cols + ")");
                                }
                            }
                        }

                        lineScanner.close();
                    }

                    System.out.println();
                    System.out.println(m);

                    break;
                case "E":
                    if (m != null) {
                        m.echelonForm();
                        System.out.println(m);
                    } else {
                        System.out.println("Must input a system first\n");
                    }
                    break;
                case "R":
                    if (m != null) {
                        m.reducedEchelonForm();
                        System.out.println(m);
                    } else {
                        System.out.println("Must input a system first\n");
                    }
                    break;
                case "Q":
                    System.out.println("Goodbye");
                    console.close();
                    break mainLoop;
                default:
                    System.out.println("Invalid input\n");
                    break;
            }
        }
    }
}
