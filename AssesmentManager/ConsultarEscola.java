package AssesmentManager;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

import AssesmentManager.Components.BotoQuadrat;

public class ConsultarEscola extends JFrame {

    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    JPanel panel;
    HashMap escolaMap;
    JComboBox escolaComboBox;
    JTextField paginaWeb;


    public ConsultarEscola() {
        setBounds(80, 120, 720, 200); // Estableix la posició i la mida de la finestra
        setTitle("Consultar Alumne"); // Estableix el títol de la finestra
        iniciarPrograma(); // Crida la funció iniciarPrograma que iniciarà tots els components de la finestra
        setVisible(true); // Fes visible la finestra

    }

    private void iniciarPrograma() {
        iniciarPanel();
        iniciarLabels();
        iniciarCombo();
        iniciarTextField();
        iniciarBotos();

    }

    private void iniciarPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        panel.setLayout(null);
    }

    private void iniciarLabels() {
        JLabel escola = new JLabel("Escola");
        escola.setBounds(20, 20, 100, 40);
        panel.add(escola);

        JLabel paginaWeb = new JLabel("Pàgina Web");
        paginaWeb.setBounds(300, 20, 100, 40);
        panel.add(paginaWeb);

    }

    private void iniciarCombo() {

        escolaComboBox = new JComboBox();
        escolaComboBox.setBounds(80, 20, 160, 40);

        escolaMap = new HashMap<>(); // Inicialitza el HashMap per emmagatzemar els noms i IDs de les escoles

        // Connexió a la base de dades
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT Escola, PaginaWeb FROM Escola ORDER BY escola";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Recórrer els resultats i afegir-los al JComboBox
            while (resultSet.next()) {
                String PaginaWeb = resultSet.getString("PaginaWeb");
                String nomEscola = resultSet.getString("Escola");
                escolaComboBox.addItem(nomEscola);
                escolaMap.put(nomEscola, PaginaWeb); // Emmagatzema l'ID associat amb el nom de l'escola
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en carregar les escoles.");
        }

        escolaComboBox.setSelectedItem(null);
        panel.add(escolaComboBox);

        escolaComboBox.addActionListener(e -> {
            String nomEscolaSeleccionada = (String) escolaComboBox.getSelectedItem();
            paginaWeb.setText((String) escolaMap.get(nomEscolaSeleccionada));
        });
    }

    private void iniciarTextField() {
        paginaWeb = new JTextField();
        paginaWeb.setBounds(400, 20, 270, 40);
        panel.add(paginaWeb);
    }

    private void iniciarBotos() {

//        Boto editar
        BotoQuadrat editar = new BotoQuadrat("Editar", 262, 100, 80, 40);
        panel.add(editar);
        editar.addActionListener(e -> {
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sql = "UPDATE Escola SET PaginaWeb = ? WHERE Escola = ?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setString(1, paginaWeb.getText());
                pstmt.setString(2, (String) escolaComboBox.getSelectedItem());

                int rowsEdited = pstmt.executeUpdate();
                if (rowsEdited > 0) {
                    JOptionPane.showMessageDialog(this, "Escola editada correctament!");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

//        Boto sortir
        BotoQuadrat sair = new BotoQuadrat("Sortir", 362, 100, 80, 40);
        panel.add(sair);

        sair.addActionListener(e -> {
            dispose();
        });

    }

}
