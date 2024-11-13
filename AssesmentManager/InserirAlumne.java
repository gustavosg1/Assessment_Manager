package AssesmentManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;

public class InserirAlumne extends JFrame {
    JPanel panel;
    InserirAssessment parent;
    CrearVentana parent2;

    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    JTextField nomField, loginField, contrasenyaField;
    JComboBox escolaComboBox;

    HashMap escolaMap;


    public InserirAlumne(InserirAssessment parent) {
        this.parent = parent;
        setBounds(80, 120, 500, 500); // Estableix la posició i la mida de la finestra
        setTitle("Inserir Alumne"); // Estableix el títol de la finestra
        iniciarPrograma(); // Crida la funció iniciarPrograma que iniciarà tots els components de la finestra
    }

    public InserirAlumne(CrearVentana parent) {
        this.parent2 = parent;
        setBounds(80, 120, 500, 500); // Estableix la posició i la mida de la finestra
        setTitle("Inserir Alumne"); // Estableix el títol de la finestra
        iniciarPrograma(); // Crida la funció iniciarPrograma que iniciarà tots els components de la finestra
    }

    public void iniciarPrograma() {
        iniciarPanel();
        iniciarLabels();
        iniciarCampsText();
        iniciarBotons();
        iniciarCombo();
    }

    public void iniciarPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
    }

    public void iniciarLabels() {
        addLabel("Nom", 20, 10, 160, 40);
        addLabel("Escola", 20, 60, 160, 40);
        addLabel("Login", 20, 110, 160, 40);
        addLabel("Contrasenya", 20, 160, 160, 40);
    }

    // Mètode per afegir una etiqueta al panell
    public void addLabel(String text, int x, int y, int width, int height) {
        JLabel item = new JLabel(text); // Crea una nova etiqueta amb el text donat
        item.setBounds(x, y, width, height); // Estableix la posició i mida de l'etiqueta
        panel.add(item); // Afegeix l'etiqueta al panell
    }

    public void iniciarCampsText() {

        nomField = new JTextField();
        nomField.setBounds(180, 10, 160, 40);
        panel.add(nomField);

        loginField = new JTextField();
        loginField.setBounds(180, 110, 160, 40);
        panel.add(loginField);

        contrasenyaField = new JTextField();
        contrasenyaField.setBounds(180, 160, 160, 40);
        panel.add(contrasenyaField);

    }

    public void iniciarCombo() {
        if (escolaComboBox == null) {
            escolaComboBox = new JComboBox();
            escolaComboBox.setBounds(180, 60, 160, 40);
            escolaComboBox.removeAllItems();
        } else {
            escolaComboBox.removeAllItems(); // Limpa as opções, mantendo o mesmo ComboBox
        }

        escolaMap = new HashMap<>(); // Inicialitza el HashMap per emmagatzemar els noms i IDs de les escoles

        // Connexió a la base de dades
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT ID_Escola, Escola FROM Escola ORDER BY escola";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Recórrer els resultats i afegir-los al JComboBox
            while (resultSet.next()) {
                int idEscola = resultSet.getInt("ID_Escola");
                String nomEscola = resultSet.getString("Escola");
                escolaComboBox.addItem(nomEscola);
                escolaMap.put(nomEscola, idEscola); // Emmagatzema l'ID associat amb el nom de l'escola
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en carregar les escoles.");
        }

        escolaComboBox.setSelectedItem(null);
        panel.add(escolaComboBox);
    }

    private void iniciarBotons() {
        JButton afegirAlumne = new JButton("Afegir Alumne");
        afegirAlumne.setBounds(180, 210, 160, 40);
        ActionListener afegirNouAlumne = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
                    String sql = "INSERT INTO clientes(nom, escola, login, contrasenya) VALUES (?, ?, ?, ?)"; // Defineix la consulta SQL per inserir dades
                    PreparedStatement statement = connection.prepareStatement(sql); // Prepara la consulta SQL

                    statement.setString(1, nomField.getText()); // Estableix el valor del camp "client"
                    String nomEscolaSeleccionada = (String) escolaComboBox.getSelectedItem();
                    statement.setInt(2, (int) escolaMap.get(nomEscolaSeleccionada));
                    statement.setString(3, loginField.getText());
                    statement.setString(4, contrasenyaField.getText());

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(InserirAlumne.this, "Alumne inserit correctament!");
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(InserirAlumne.this, "Error en inserir l'alumne.");
                }

                if (parent != null) {
                    parent.iniciarCombo();
                }
            }

        };
        panel.add(afegirAlumne);
        afegirAlumne.addActionListener(afegirNouAlumne);

        // Inicialitza botó 'Nova Escola'

        JButton novaEscola = new JButton("Nova Escola");
        novaEscola.setBounds(370, 65, 80, 30);
        novaEscola.setMargin(new Insets(0, 0, 0, 0));
        novaEscola.setFont(new Font("Arial", Font.BOLD, 9));
        panel.add(novaEscola);

        ActionListener inserirNovaEscola = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InserirEscola v5 = new InserirEscola(InserirAlumne.this);
                v5.setVisible(true);
            }
        };

        novaEscola.addActionListener(inserirNovaEscola);
    }

}

