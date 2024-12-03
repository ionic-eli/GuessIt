package Model;

public class UserVideogame extends Usuario
{
    public UserStats userStats;
    
    // constructor típico para el registro de un usuario el cual tiene un parámetro de tipo string
    public UserVideogame(String username) 
    {
        // Inicializando los datos de un nuevo usuario
        super(username);
        userStats = new UserStats();
        userStats.setVecesUsadoSuma(0);
        userStats.setVecesUsadoResta(0);
        userStats.setVecesUsadoMul(0);
        userStats.setVecesUsadosDiv(0);
        userStats.createTimeList(); // Método que hace new a la lista con el tiempo que le llevó a un alumno hacer la operación
        userStats.setScore(0);
    }
}
