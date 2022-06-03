import java.text.DecimalFormat;

public class Main {
    static void showMatrix(double[][] matrix, double[] answers)
    {
        System.out.println();
        for (int row = 0; row < matrix.length; row++)
        {
            for (int col = 0; col < matrix[row].length; col++)
            {
                System.out.print(" " + matrix[row][col] + " ");
            }
            System.out.print("\t " + answers[row] + " ");
            System.out.println();
        }
        System.out.println();

    }

    static double[] GaussianElimination(double[][] matrix, double[] rightFromEquals)
    {
        double[] result = new double[matrix.length];

        int length = matrix.length;

        //Echelon Form
        for (int i = 0; i < length-1; i++)
        {
            int max = i;

            for (int row = i+1; row < length; row++)
            {
                if (Math.abs(matrix[max][i]) < Math.abs(matrix[row][i]))
                {
                    max = row;
                }
            }

            var tempRow = matrix[max];
            matrix[max] = matrix[i];
            matrix[i] = tempRow;

            var tempRowForEquals = rightFromEquals[max];
            rightFromEquals[max] = rightFromEquals[i];
            rightFromEquals[i] = tempRowForEquals;

            showMatrix(matrix, rightFromEquals);

            for (int row = i + 1; row < length; row++)
            {
                double ratio = matrix[row][i] / matrix[i][i];

                for (int col = i; col < length; col++)
                {
                    matrix[row][col] = matrix[row][col] - ratio * matrix[i][col];
                }
                rightFromEquals[row] -= ratio * rightFromEquals[i];

                showMatrix(matrix, rightFromEquals);

            }
        }

        for (int i = length-1; i >= 0; i--)
        {
            double sum = 0;
            for (int col = length-1; col > i; col--)
            {
                sum += result[col] * matrix[i][col];
            }
            result[i] = (rightFromEquals[i] - sum) / matrix[i][i];
        }

        return result;
    }
    public static void main(String[] args) {
        //EXAMPLE
        // x + y - z = -2
        // 2x - y + z = 5
        // -x + 2y + 2z = 1
        // result: x = 1, y = -1, z = 2

        double[][] matrix =
                {
                        new double[] { 1,1,-1},
                        new double[] {2,-1,1},
                        new double[] { -1,2,2 }
                };

        double[] rightFromEquals = { -2,5,1 };

        double[] result = GaussianElimination(matrix, rightFromEquals);

        char[] resultVariables = { 'x', 'y', 'z' };

        DecimalFormat df = new DecimalFormat("0.000");

        for (int i = 0; i < resultVariables.length; i++)
        {
            System.out.println(String.format("%c = %s", resultVariables[i], df.format(result[i])));
        }
    }
}
