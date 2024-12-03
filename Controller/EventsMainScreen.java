package Controller;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import javax.swing.*;

import Model.UserManager;
import Model.UserVideogame;
import View.SelectUserToPlay;
import View.Scoreboard;

public class EventsMainScreen 
{
    static String msg = "";
    static String titulo = "";
    private static int USERNAME_LEN = 8;
    public static UserManager userManager; // referencia estática para manejar usuarios
    private static JFrame mainMenuFrame; // Almacena el marco principal
    private static Clip audioClip; // Almacena la música de fondo

    public EventsMainScreen(UserManager userManager) {
        EventsMainScreen.userManager = userManager;
    }

    // eventos / métodos de cada botón de la clase mainscreen

    public static void startGame(JFrame frame, Clip audio) 
    {
        // inicializar referencias al menú principal y al clip de audio
        if(userManager.getUsersList().size() != 0)
        {
            mainMenuFrame = frame;
            audioClip = audio;
            new SelectUserToPlay(userManager, mainMenuFrame, audioClip); // inicializa la selección de usuario para comenzar el juego
        }
        else
        {
            msg = "Registre a mínimo un usuario para jugar.";
            titulo = "No hay usuarios registrados en la aplicación.";
            JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void showScoreboard() {
        // muestra la tabla de puntuaciones con los datos de usuarios
        new Scoreboard().leerUsuarios(userManager.getUsersList());
    }

    public static int registerNewUser() 
    {
        ArrayList<UserVideogame> users = userManager.getUsersList();

        String username = JOptionPane.showInputDialog
        (
            null,
            "Ingresa el nombre del usuario que quieres registrar",
            "Registro de usuario",
            JOptionPane.QUESTION_MESSAGE
        );
        username = username.trim();

        if(username == null)
        {
            // Significa que el usuario nomás le dio a "Cancel"
            return 0;
        }

        if (username.isEmpty()) 
        {
            // El usuario le dió click a 'Ok' pero sin llenar el input
            msg = "Necesita ingresar un nombre para poder registrarse.";
            titulo = "Campo de texto vacío";
            JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
            return 0;
        }

        if(username.length() > USERNAME_LEN)
        {
            // El usuario le dió click a 'Ok' pero sin llenar el input
            msg = "El nombre de usuario supera los 8 caracteres";
            titulo = "Nombre de usuario demasiado largo";
            JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
            return 0;
        }

        for (UserVideogame user : users) 
        {
            if (user.getUsername().equalsIgnoreCase(username)) 
            {
                msg = "Ese nombre de usuario ya está registrado. Intente con otro nombre.";
                titulo = "Usuario ya registrado";
                JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
                return 0;
            }
        }

        userManager.newUser(new UserVideogame(username.trim()));
        msg = "El usuario ha sido registrado exitosamente.";
        titulo = "Registro exitoso";
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
        return 1;
    }

    public static void closeApp(Clip audio) {
        if (userManager.getUsersList().size() > 0) // Si hay más de un usuario registrado, aunque solo se haya metido a registrar, hay que serializar.
        {
            ReadWriteData.writeData(userManager.getUsersList());
        }

        if (audio != null) 
        {
            audio.stop();
            audio.close();
        }
        System.exit(0);
    }
}
