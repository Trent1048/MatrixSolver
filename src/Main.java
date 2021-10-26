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

            command = console.next().toUpperCase();

            switch (command) {
                case "I":

                    System.out.print("How many rows? ");
                    rows = console.nextInt();
                    System.out.print("How many columns? ");
                    cols = console.nextInt();

                    m = new Matrix(rows, cols);

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
