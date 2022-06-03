import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner inputObject = new Scanner(System.in);

        while(true) {

            PrintChoiceMenu();
            String choice = inputObject.nextLine();

            int n = 0;
            int k = 0;

            Pattern p = Pattern.compile("[1-3]");

            if(!p.matcher(choice).matches()) {
                break;
            }

            switch (choice) {
                case "1" -> {
                    System.out.print("n = ");
                    n = Integer.parseInt(inputObject.nextLine());
                    permutation(n);
                }
                case "2" -> {
                    System.out.print("n = ");
                    n = Integer.parseInt(inputObject.nextLine());
                    System.out.print("k = ");
                    k = Integer.parseInt(inputObject.nextLine());
                    variation(n, k);
                }
                case "3" -> {
                    System.out.print("n = ");
                    n = Integer.parseInt(inputObject.nextLine());
                    System.out.print("k = ");
                    k = Integer.parseInt(inputObject.nextLine());
                    combination(n, k);
                }
            }
        }

    }

    static int permutation(int n) {
        int result = 1;

        for (int i = 2; i <= n; i++) {
            result *= i;
        }

        System.out.println("\nResult for permutation: " + result + "\n");

        return result;
    }

    static int variation(int n, int k) {
        int result = 1;

        for (int i = n, counter = 1; counter <= k; i--, counter++) {
            result *= i;
        }

        System.out.println("\nResult for variation: " + result + "\n");

        return result;
    }

    static void combination(int n, int k) {
        System.out.println("\nResult for variation: " + variation(n, k) / permutation(k) + "\n");
    }

    static void PrintChoiceMenu() {
        System.out.println("1. Permutation");
        System.out.println("2. Variation");
        System.out.println("3. Combination");
        System.out.println("Anything else to exit");
        System.out.print("Choice: ");
    }
}
