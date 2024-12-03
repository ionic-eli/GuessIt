package Model;
import java.util.Random;

public class MatrixGenerator 
{
    public static int[][] generateMatrix(int dimensiones, int min, int max)
    {
        Random rand = new Random();
        int[][] matrix = new int[dimensiones][dimensiones];

        // rellenar con numeros aleatorios entre min y max;
        for(int i = 0; i < dimensiones; i++)
        {
            for(int j = 0; j < dimensiones; j++)
            {
                matrix[i][j] = rand.nextInt(max - min + 1) + min;
            }
        }
        return matrix;
    }
}