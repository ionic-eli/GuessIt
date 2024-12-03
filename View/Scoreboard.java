package View;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Model.UserVideogame;

public class Scoreboard {

    @SuppressWarnings("unused")
    public void leerUsuarios(ArrayList<UserVideogame> usuariosDelJuego) {
        int cantidadDeUsuarios = usuariosDelJuego.size();

        // verificar que existan usuarios registrados
        if (cantidadDeUsuarios > 0) {
            // configuraciones del frame de la tabla de puntuaciones
            JFrame scoreboardFrame = new JFrame("Tabla de Puntuaciones");
            scoreboardFrame.setSize(800, 600);
            scoreboardFrame.setLocationRelativeTo(null);
            scoreboardFrame.setUndecorated(true);

            // definir los encabezados de la tabla
            String[] header = {
                "Nombre de Usuario", "Puntaje", 
                "Veces Usado: +", "Veces Usado: -", "Veces Usado: *", "Veces Usado: /"
            };

            // llenar los datos de la tabla
            Object[][] data = new Object[cantidadDeUsuarios][header.length];
            
            for (int i = 0; i < cantidadDeUsuarios; i++) 
            {
                UserVideogame user = usuariosDelJuego.get(i);
                data[i][0] = user.getUsername();
                data[i][1] = user.userStats.getScore();
                data[i][2] = user.userStats.getVecesUsadoSuma();
                data[i][3] = user.userStats.getVecesUsadoResta();
                data[i][4] = user.userStats.getVecesUsadoMul();
                data[i][5] = user.userStats.getVecesUsadosDiv();
            }

            // crear la tabla con los datos
            DefaultTableModel model = new DefaultTableModel(data, header);
            JTable scoresTable = new JTable(model);
            scoresTable.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(scoresTable);

            // panel superior con etiqueta "Tabla de Puntaje"
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel titleLabel = new JLabel("Tabla de Puntaje");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            topPanel.add(titleLabel);

            JPanel flowPanel = new JPanel(new FlowLayout());

            JButton closeScoreboardFrame = new JButton("Cerrar");
            closeScoreboardFrame.setPreferredSize(new Dimension(100, 40));
            closeScoreboardFrame.setForeground(Color.WHITE);
            closeScoreboardFrame.setBackground(Color.RED);
            closeScoreboardFrame.addActionListener(e -> scoreboardFrame.dispose());
            flowPanel.add(closeScoreboardFrame);

            // agregar componentes al frame
            scoreboardFrame.setLayout(new BorderLayout());
            scoreboardFrame.add(topPanel, BorderLayout.NORTH);
            scoreboardFrame.add(scrollPane, BorderLayout.CENTER);
            scoreboardFrame.add(flowPanel, BorderLayout.SOUTH);
            scoreboardFrame.setVisible(true);
        } else {
            // mensaje de error si no hay usuarios
            JOptionPane.showMessageDialog(null, "¡No hay jugadores registrados en la aplicación!", 
                                          "No hay usuarios registrados", JOptionPane.ERROR_MESSAGE);
        }
    }
}
