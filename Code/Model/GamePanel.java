package Code.Model;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.sound.sampled.Clip;
import javax.swing.*;

import Code.Controller.MusicLoader;
import Code.View.GameScreen;

public class GamePanel extends JPanel 
{
    private int[][] matrix;
    private boolean[][] usedCells;
    private int targetNum;
    private int currentNum;
    private String currentOperation = "+";
    private final GameScreen parent;
    private final UserVideogame userPlaying;
    private final GameScreen.ClockClass threadReference;

    private int hoverRow = -1; // rastrear fila donde se encuentra el cursor
    private int hoverCol = -1; // rastrear columna donde se encuentra el cursor
    
    // Control de las veces que se usaron los operadores
    int sumaPressed = 0;
    int restaPressed = 0;
    int mulPressed = 0;
    int divPressed = 0;

    public GamePanel(GameScreen parent, UserVideogame userPlaying, GameScreen.ClockClass threadReference) 
    {
        this.parent = parent;
        this.userPlaying = userPlaying;
        this.threadReference = threadReference;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.WHITE);

        matrix = MatrixGenerator.generateMatrix(4, 0, 10); // incluir 0 en la matriz
        usedCells = new boolean[matrix.length][matrix[0].length];
        targetNum = MatrixGenerator.generateMatrix(1, 20, 50)[0][0];
        currentNum = 0;
        parent.updateLabels(targetNum, currentNum, currentOperation);

        // agregar listener para el hover
        addMouseMotionListener(new MouseAdapter() 
        {
            @Override
            public void mouseMoved(MouseEvent e) 
            {
                updateHoverCell(e.getX(), e.getY());
            }
        });

        // agregar listener para el clic del ratón
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                Clip clickedButton = MusicLoader.loadASong("C:/Users/adria/OneDrive/Desktop/uabc/semestre_4/LPOO/GuessIt/audios/select.wav");
                clickedButton.start();
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void updateHoverCell(int mouseX, int mouseY) 
    {
        int gridSize = calculateGridSize();
        int paddingX = (getWidth() - gridSize * matrix[0].length) / 2; // centrar horizontalmente
        int paddingY = (getHeight() - gridSize * matrix.length) / 2;  // centrar verticalmente

        int tentativeCol = (mouseX - paddingX) / gridSize;
        int tentativeRow = (mouseY - paddingY) / gridSize;

        // validar si el mouse está dentro del área de la matriz
        if (tentativeRow >= 0 && tentativeRow < matrix.length &&
            tentativeCol >= 0 && tentativeCol < matrix[0].length &&
            mouseX >= paddingX && mouseX < paddingX + gridSize * matrix[0].length &&
            mouseY >= paddingY && mouseY < paddingY + gridSize * matrix.length) {
            hoverRow = tentativeRow;
            hoverCol = tentativeCol;
        } else {
            hoverRow = -1;
            hoverCol = -1; // reiniciar hover si está fuera de los límites
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        int gridSize = calculateGridSize();
        int paddingX = (getWidth() - gridSize * matrix[0].length) / 2; // centrar horizontalmente
        int paddingY = (getHeight() - gridSize * matrix.length) / 2;  // centrar verticalmente

        for (int i = 0; i < matrix.length; i++) 
        {
            for (int j = 0; j < matrix[i].length; j++) 
            {
                int x = j * gridSize + paddingX;
                int y = i * gridSize + paddingY;

                // resaltar celda cuando el cursor está encima
                if (i == hoverRow && j == hoverCol) {
                    g.setColor(Color.YELLOW);
                } else if (usedCells[i][j]) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(x, y, gridSize, gridSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, gridSize, gridSize);
                g.drawString(Integer.toString(matrix[i][j]), x + gridSize / 3, y + gridSize / 2);
            }
        }
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        int gridSize = calculateGridSize();
        int paddingX = (getWidth() - gridSize * matrix[0].length) / 2; // centrar horizontalmente
        int paddingY = (getHeight() - gridSize * matrix.length) / 2;

        int col = (mouseX - paddingX) / gridSize;
        int row = (mouseY - paddingY) / gridSize;

        if (row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length && !usedCells[row][col]) {
            int value = matrix[row][col];
            usedCells[row][col] = true;

            // actualizar operación
            switch (currentOperation) 
            {
                case "+": 
                    currentNum += value;
                    sumaPressed++;
                    break;

                case "-": 
                    currentNum -= value;
                    restaPressed++;
                    break;

                case "*": 
                    currentNum *= value;
                    mulPressed++;
                    break;

                case "/":
                    if(value != 0)
                    {
                        currentNum /= value;
                        divPressed++;
                    } 
                    else
                    {
                        JOptionPane.showMessageDialog(this, "No se puede dividir por cero.");
                    }
                    break;
            }

            if (currentNum == targetNum) 
            {
                Clip winAudio = MusicLoader.loadASong("C:/Users/adria/OneDrive/Desktop/uabc/semestre_4/LPOO/GuessIt/audios/win.wav"); // Activando la canción de la celebración
                winAudio.start(); 
                threadReference.resetClock(); // Reiniciando el hilo

                // 100 puntos por cada numero adivinado + un bonus por cada operador matemático usado
                userPlaying.userStats.setVecesUsadoSuma(userPlaying.userStats.getVecesUsadoSuma() + sumaPressed);
                userPlaying.userStats.setVecesUsadoResta(userPlaying.userStats.getVecesUsadoResta() + restaPressed);
                userPlaying.userStats.setVecesUsadoMul(userPlaying.userStats.getVecesUsadoMul() + mulPressed);
                userPlaying.userStats.setVecesUsadosDiv(userPlaying.userStats.getVecesUsadosDiv() + divPressed);

                int formulaForScore = (int) ((userPlaying.userStats.getScore() + 100) + (sumaPressed * 0.1) + (restaPressed * 0.2) + (mulPressed * 0.3) + (divPressed * 0.4)); 
                userPlaying.userStats.setScore(formulaForScore); 
                userPlaying.userStats.setTiempoPorNumero(parent.getTimer()); // Agregando el tiempo que se demoró el usuario en hacer la operación
                
                // Actualizando la información del panel
                targetNum = MatrixGenerator.generateMatrix(1, 20, 50)[0][0];
                matrix = MatrixGenerator.generateMatrix(matrix.length + 1, 0, 10);
                usedCells = new boolean[matrix.length][matrix[0].length];
                currentNum = 0;
                
                // Reiniciando la cantidad de veces que se usó algo
                sumaPressed = 0;
                restaPressed = 0;
                mulPressed = 0;
                divPressed = 0;
            }
            parent.updateLabels(targetNum, currentNum, "+");
            repaint();
        }
    }

    private int calculateGridSize() 
    {
        return Math.min(getWidth() / matrix[0].length, getHeight() / matrix.length);
    }

    @SuppressWarnings("unused")
    public JPanel createRefreshButtonPanel()
    {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        JButton refreshButton = new JButton("Refrescar");
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(Color.BLUE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(e -> refresh());
        bottomPanel.add(refreshButton);
        return bottomPanel;
    }

    public void refresh() 
    {
        currentNum = 0;
        setCurrentOperation("+");
        matrix = MatrixGenerator.generateMatrix(matrix.length, 0, 10); // resetear el tablero
        usedCells = new boolean[matrix.length][matrix[0].length];
        parent.updateLabels(targetNum, currentNum, "+"); // NO BORRAR ESTA LINEA POR FA
        repaint();
    }

    // Getters
    public int getTargetNum() {
        return targetNum;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentOperation(String operation) 
    {
        currentOperation = operation;
    }
}