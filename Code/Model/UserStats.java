package Code.Model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class UserStats implements Serializable
{
    private int score; // puntaje del usuario
    // Cantidad de veces que se usó cada operador
    private int vecesUsadoSuma;
    private int vecesUsadoResta;
    private int vecesUsadoMul;
    private int vecesUsadoDiv;
    private ArrayList<String> tiempoPorNumero; // arreglo con la cantidad de tiempo por cada número objetivo acertado

    public int getVecesUsadoSuma() {
    return vecesUsadoSuma;
    }

    public void setVecesUsadoSuma(int vecesUsadoSuma) {
        this.vecesUsadoSuma = vecesUsadoSuma;
    }

    public int getVecesUsadoResta() {
        return vecesUsadoResta;
    }

    public void setVecesUsadoResta(int vecesUsadoResta) {
        this.vecesUsadoResta = vecesUsadoResta;
    }

    public int getVecesUsadoMul() {
        return vecesUsadoMul;
    }

    public void setVecesUsadoMul(int vecesUsadoMul) {
        this.vecesUsadoMul = vecesUsadoMul;
    }

    public int getVecesUsadosDiv() {
        return vecesUsadoDiv;
    }

    public void setVecesUsadosDiv(int vecesUsadosDiv) {
        this.vecesUsadoDiv = vecesUsadosDiv;
    }

    public void createTimeList()
    {
        tiempoPorNumero = new ArrayList<>();
    }

    public ArrayList<String> getTiempoPorNumero() {
        if (tiempoPorNumero == null) {
            tiempoPorNumero = new ArrayList<>();
        }
        return tiempoPorNumero;
    }

    public void setTiempoPorNumero(String tiempo) {
        getTiempoPorNumero().add(tiempo);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // método para inicializar campos transitorios después de la deserialización
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException 
    {
        ois.defaultReadObject(); // deserializa los campos no transitorios
        if (tiempoPorNumero == null) {
            tiempoPorNumero = new ArrayList<>();
        }
    }
}