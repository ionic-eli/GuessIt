package Code.View;
import java.awt.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

import Code.Model.GamePanel;
import Code.Model.UserManager;
import Code.Model.UserVideogame;

public class GameScreen 
{
    private JFrame gameScreenFrame;
    private GamePanel gamePanel;
    private JLabel targetLabel;
    private JLabel timerLabel;
    private JLabel currentLabel;
    private JLabel operationLabel;
    private UserManager userManager; // reference to UserManager
    private boolean isMainMenuOpened = false; // Prevent multiple main menu instances

    @SuppressWarnings("unused")
    public GameScreen(UserVideogame userPlaying, UserManager userManager) 
    {
        this.userManager = userManager; // store the reference to UserManager

        // configuración principal de la ventana
        gameScreenFrame = new JFrame();
        gameScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameScreenFrame.setUndecorated(true); 
        gameScreenFrame.setResizable(false);
        gameScreenFrame.setFocusCycleRoot(true);
        gameScreenFrame.setSize(1150, 750);
        gameScreenFrame.setLayout(new BorderLayout());

        // Creando los paneles y sus componentes internos
        JPanel mainPanel = new JPanel(new BorderLayout()); // Panel principal

        // PANEL NORTE
        
        JPanel northPanel = new JPanel(new GridLayout(1, 2));
        timerLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton returnToMenu = new JButton("Volver al menú");

        returnToMenu.setPreferredSize(new Dimension(150, 30));
        
        JPanel foo = new JPanel();
        foo.add(returnToMenu);
       
        returnToMenu.addActionListener(e -> returnToMainMenu());
        returnToMenu.setFont(new Font("Arial", Font.BOLD, 13));
        returnToMenu.setForeground(Color.WHITE);
        returnToMenu.setBackground(Color.RED);

        // Hilo del reloj
        ClockClass clockThread = new ClockClass(timerLabel);
        clockThread.start();

        northPanel.add(timerLabel);
        northPanel.add(foo);
        mainPanel.add(BorderLayout.NORTH, northPanel);

        // PANEL OESTE
        JPanel westPanel = new JPanel(new GridLayout(6, 1, 150, 10));
        JLabel userLabel = createLabel("Usuario: " + userPlaying.getUsername());
        targetLabel = createLabel("Objetivo");
        currentLabel = createLabel("Operando");
        operationLabel = createLabel("Operador: +");
        westPanel.add(userLabel);
        westPanel.add(targetLabel);
        westPanel.add(currentLabel);
        westPanel.add(operationLabel);
        
        // PANEL CENTRAL
        gamePanel = new GamePanel(this, userPlaying, clockThread); // pass GameScreen and UserVideogame
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0)); // Dynamic padding
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        westPanel.add(gamePanel.createRefreshButtonPanel()); // último elemento del panel oeste
        mainPanel.add(BorderLayout.WEST, westPanel);

        // PANEL ESTE
        JPanel operatorsPanel = createOperatorPanel();
        mainPanel.add(operatorsPanel, BorderLayout.EAST);

        // PANEL SUR = Creado en la clase GamePanel

        // agregar todo al frame
        gameScreenFrame.add(mainPanel);
        gameScreenFrame.setLocationRelativeTo(null); // centrar la ventana en la pantalla
        gameScreenFrame.setVisible(true);
    }

    // método para crear etiquetas
    private JLabel createLabel(String text) 
    {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    // método para crear el panel de operadores
    @SuppressWarnings("unused")
    private JPanel createOperatorPanel() 
    {
        JPanel operatorsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        String[] operators = {"+", "-", "*", "/"};

        for (String operator : operators) 
        {
            JButton operatorButton = new JButton(operator);
            operatorButton.setFont(new Font("Arial", Font.BOLD, 20));

            operatorButton.addActionListener(e -> {
                gamePanel.setCurrentOperation(operator);
                operationLabel.setText("Operador: " + operator);
            });
            operatorsPanel.add(operatorButton);
        }
        operatorsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20)); // padding for spacing
        return operatorsPanel;
    }

    // método para volver al menú principal
    public void returnToMainMenu() 
    {
        if (!isMainMenuOpened) 
        {
            isMainMenuOpened = true;
            gameScreenFrame.dispose(); // cerrar la pantalla del juego
            new MainScreen(userManager); // abrir el menú principal
        }
    }

    public void updateLabels(int target, int current, String currentOperation) 
    {
        targetLabel.setText("Objetivo: " + target);
        currentLabel.setText("Operando: " + current);
        operationLabel.setText("Operador: " + currentOperation);
        gameScreenFrame.repaint();
    }

    public String getTimer()
    {
        return timerLabel.getText();
    }

    public class ClockClass extends Thread 
    {
        private JLabel timerLabel;
        private boolean running = true;
        private boolean reset = false;
        private int segundos = 0;
        private int minutos = 0;
        private int horas = 0;

        ClockClass(JLabel timerLabel) {
            this.timerLabel = timerLabel;
        }

        public synchronized void resetClock() {
            segundos = 0;
            minutos = 0;
            horas = 0;
            reset = true; // Indicar que se reinició el reloj
        }

        public synchronized void stopClock() {
            running = false;
            interrupt(); // Detener el hilo si está bloqueado
        }

        public void run()
        {
            while(running)
            {
                synchronized(this)
                {
                    if(reset)
                    {
                        reset = false;
                    }
                }

                String time = String.format("%02d:%02d:%02d", horas, minutos, segundos);
                SwingUtilities.invokeLater(() -> timerLabel.setText(time));

                try {
                    TimeUnit.SECONDS.sleep(1);;
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }

                segundos++;
                if(segundos == 60)
                {
                    segundos = 0;
                    minutos++;
                    if(minutos == 60)
                    {
                        minutos = 0;
                        horas++;
                    }
                }
            }
        }
    }
}