package Code.Model;

// Clase para modelar a un usuario de la vida real
import java.io.Serializable;

public class Usuario implements Serializable
{
    // Atributos
    private String username;
    
    Usuario(String username)
    {
        this.username = username;
    }
    // Setters y getters 

    public void setUsername(String username) 
    {
        this.username = username;
    }
    
    public String getUsername() 
    {
        return username;
    }
}