package Code.View;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

import Code.Controller.EventsMainScreen;
import Code.Controller.MusicLoader;
import Code.Controller.ReadWriteData;
import Code.Model.UserManager;


public class MainScreen {
    private final UserManager userManager; // referencia al administrador de usuarios para la serialización
    
    public MainScreen(UserManager userManager) 
    {
        this.userManager = userManager; // guardar referencia al administrador de usuarios
        new EventsMainScreen(userManager); // dandole la lista a la clase que maneja los eventos
        createMainScreen(); // este método construye el menú de inicio
    }
    
    @SuppressWarnings("unused") // esto es para los lambdas de los actionlisteners de los botones del menú.
    private void createMainScreen() 
    {
        JFrame mainFrame = new JFrame("");
        JPanel panelBL = new JPanel(new BorderLayout());
        JPanel columnGridPanel = new JPanel(new GridLayout(4, 1));
        int widthImg = 300;
        int heigthImg = 300;
        
        // configuración principal de la ventana
        mainFrame.setUndecorated(true);
        mainFrame.setResizable(false);
        mainFrame.setFocusCycleRoot(true);
        
        try 
        {
            // cargando la canción del menú de inicio
            Clip songMenu = MusicLoader.loadASong("C:/Users/adria/OneDrive/Desktop/uabc/semestre_4/LPOO/GuessIt/audios/song.wav");
            songMenu.start();

            // procesamiento de la imagen del logo
            BufferedImage originalImage = ImageIO.read(new File("C:/Users/adria/OneDrive/Desktop/uabc/semestre_4/LPOO/GuessIt/images/Logo.png"));
            BufferedImage logoGuessIt = new BufferedImage(widthImg, heigthImg, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = logoGuessIt.createGraphics();
            g.drawImage(originalImage.getScaledInstance(widthImg, heigthImg, Image.SCALE_SMOOTH), 0, 0, null);

            // componentes de la interfaz gráfica de usuario
            JLabel picLabel = new JLabel(new ImageIcon(logoGuessIt));
            JButton buttonStartGame = new JButton("Iniciar juego");
            JButton buttonRegisterUser = new JButton("Registrar un nuevo usuario");
            JButton buttonShowScoreboard = new JButton("Tabla de puntuaciones");
            JButton buttonExit = new JButton("Salir");

            // propiedades de los botones
            Dimension buttonSize = new Dimension(10, 50);
            buttonStartGame.setForeground(Color.WHITE);
            buttonStartGame.setBackground(Color.GREEN);
            buttonExit.setForeground(Color.WHITE);
            buttonExit.setBackground(Color.RED);

            buttonStartGame.setPreferredSize(buttonSize);
            buttonRegisterUser.setPreferredSize(buttonSize);
            buttonShowScoreboard.setPreferredSize(buttonSize);
            buttonExit.setPreferredSize(buttonSize);

            // eventos de los botones
            buttonStartGame.addActionListener(e -> EventsMainScreen.startGame(mainFrame, songMenu));
            buttonRegisterUser.addActionListener(e -> EventsMainScreen.registerNewUser());
            buttonShowScoreboard.addActionListener(e -> EventsMainScreen.showScoreboard());
            buttonExit.addActionListener(e -> EventsMainScreen.closeApp(songMenu));

            // agregando cada componente al panel
            panelBL.add(BorderLayout.NORTH, picLabel);
            columnGridPanel.add(buttonStartGame);
            columnGridPanel.add(buttonRegisterUser);
            columnGridPanel.add(buttonShowScoreboard);
            columnGridPanel.add(buttonExit);
            panelBL.add(BorderLayout.CENTER, columnGridPanel);
            mainFrame.add(panelBL);

            // manejar el evento de cerrar la ventana con el botón "X"
            mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    // guardar los datos de usuario antes de salir
                    ReadWriteData.writeData(userManager.getUsersList());
                    System.exit(0); // salir de la aplicación
                }
            });

            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null); // centrar la ventana en la pantalla
            mainFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
