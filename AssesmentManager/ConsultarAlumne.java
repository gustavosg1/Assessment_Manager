package AssesmentManager;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

import AssesmentManager.Components.BotoQuadrat;

public class ConsultarAlumne extends JFrame {

    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    JPanel panel;
    HashMap escolaComboAlumneMap, escolaComboEscolaMap, loginMap, contrasenyaMap;
    JComboBox alumneComboBox, escolaComboBox;
    JTextField login, contrasenya;

    public ConsultarAlumne() {
        setBounds(80, 120, 660, 280); // Estableix la posició i la mida de la finestra
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
        JLabel alumne = new JLabel("Alumne");
        alumne.setBounds(10, 20, 60, 40);
        panel.add(alumne);

        JLabel escola = new JLabel("Escola");
        escola.setBounds(10, 100, 60, 40);
        panel.add(escola);

        JLabel login = new JLabel("Login");
        login.setBounds(270, 20, 80, 40);
        panel.add(login);

        JLabel contrasenya = new JLabel("Contrasenya");
        contrasenya.setBounds(270, 100, 80, 40);
        panel.add(contrasenya);

    }

    private void iniciarCombo() {

//        Iniciar combo alumne

        alumneComboBox = new JComboBox();
        alumneComboBox.setBounds(80, 20, 160, 40);

        escolaComboAlumneMap = new HashMap();
        loginMap = new HashMap();
        contrasenyaMap = new HashMap();

//        Connexió a la base de dades
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "select c.ID_Cliente, c.Nom, e.Escola, c.Login, c.Contrasenya from clientes as c\n" +
                    "JOIN escola as e\n" +
                    "WHERE c.Escola = e.ID_Escola\n" +
                    "ORDER BY Nom";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Recórrer els resultats i afegir-los al JComboBox
            while (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String escola = resultSet.getString("Escola");
                String login = resultSet.getString("Login");
                String contrasenya = resultSet.getString("Contrasenya");

                alumneComboBox.addItem(nom);
                escolaComboAlumneMap.put(nom, escola);
                loginMap.put(nom, login);
                contrasenyaMap.put(nom, contrasenya);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en carregar les escoles.");
        }

        alumneComboBox.setSelectedItem(null);
        panel.add(alumneComboBox);

        alumneComboBox.addActionListener(e -> {
            String nomEscolaSeleccionada = (String) alumneComboBox.getSelectedItem();
            escolaComboBox.setSelectedItem(escolaComboAlumneMap.get(nomEscolaSeleccionada));
            login.setText((String) loginMap.get(nomEscolaSeleccionada));
            contrasenya.setText((String) contrasenyaMap.get(nomEscolaSeleccionada));
        });

//        Iniciar combo escola

        escolaComboBox = new JComboBox();
        escolaComboBox.setBounds(80, 100, 160, 40);
        escolaComboEscolaMap = new HashMap();

        // Connexió a la base de dades
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT ID_Escola, Escola FROM Escola ORDER BY escola";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Recórrer els resultats i afegir-los al JComboBox
            while (resultSet.next()) {

                int ID_Escola = resultSet.getInt("ID_Escola");
                String Escola = resultSet.getString("Escola");

                escolaComboBox.addItem(Escola);
                escolaComboEscolaMap.put(Escola, ID_Escola);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en carregar les escoles.");
        }

        escolaComboBox.setSelectedItem(null);
        panel.add(escolaComboBox);
    }

    private void iniciarTextField() {
//           Camp login
        login = new JTextField();
        login.setBounds(360, 20, 240, 40);
        panel.add(login);

//           Camp contrasenya
        contrasenya = new JTextField();
        contrasenya.setBounds(360, 100, 240, 40);
        panel.add(contrasenya);
    }

    private void iniciarBotos() {

//        Boto editar
        BotoQuadrat editar = new BotoQuadrat("Editar", 232, 180, 80, 40);
        panel.add(editar);
        editar.addActionListener(e -> {
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sql = "UPDATE Clientes SET Escola = ?, Login = ?, Contrasenya = ? WHERE Nom = ?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setInt(1, (int) escolaComboEscolaMap.get(escolaComboBox.getSelectedItem()));
                pstmt.setString(2, (String) login.getText());
                pstmt.setString(3, (String) contrasenya.getText());
                pstmt.setString(4, (String) alumneComboBox.getSelectedItem());


                int rowsEdited = pstmt.executeUpdate();
                if (rowsEdited > 0) {
                    JOptionPane.showMessageDialog(this, "Escola editada correctament!");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

//        Boto sortir
        BotoQuadrat sair = new BotoQuadrat("Sortir", 332, 180, 80, 40);
        panel.add(sair);

        sair.addActionListener(e -> {
            dispose();
        });

    }

}
