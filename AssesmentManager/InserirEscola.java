package AssesmentManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InserirEscola extends JFrame {
    JPanel panel;
    InserirAlumne parent;

    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    JTextField escolaField;
    JTextField paginaField;

    public InserirEscola(InserirAlumne parent) {
        this.parent = parent;
        setBounds(80, 120, 600, 200); // Estableix la posició i la mida de la finestra
        setTitle("Inserir Escola"); // Estableix el títol de la finestra
        iniciarPrograma(); // Crida la funció iniciarPrograma que iniciarà tots els components de la finestra
    }

    public InserirEscola() {
        setBounds(80, 120, 600, 200); // Estableix la posició i la mida de la finestra
        setTitle("Inserir Escola"); // Estableix el títol de la finestra
        iniciarPrograma(); // Crida la funció iniciarPrograma que iniciarà tots els components de la finestra
    }

    public void iniciarPrograma() {
        iniciarPanel();
        iniciarLabels();
        iniciarCampsText();
        iniciarBotons();
    }


    public void iniciarPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
    }

    public void iniciarLabels() {
        JLabel escola = new JLabel("Nom Escola");
        escola.setBounds(20, 20, 80, 40);
        panel.add(escola);

        JLabel pagina = new JLabel("Pàgina Web");
        pagina.setBounds(300, 20, 80, 40);
        panel.add(pagina);

    }

    public void iniciarCampsText() {
        escolaField = new JTextField();
        escolaField.setBounds(100, 20, 160, 40);
        panel.add(escolaField);

        paginaField = new JTextField();
        paginaField.setBounds(400, 20, 160, 40);
        panel.add(paginaField);

    }

    private void iniciarBotons() {

        JButton afegirEscola = new JButton("Afegir Escola");
        afegirEscola.setBounds(220, 80, 160, 40);
        afegirEscola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
                    String sql = "INSERT INTO escola (escola, paginaweb) VALUES (?, ?)"; // Defineix la consulta SQL per inserir dades
                    PreparedStatement statement = connection.prepareStatement(sql); // Prepara la consulta SQL

                    statement.setString(1, escolaField.getText()); // Estableix el valor del camp "client"
                    statement.setString(2, paginaField.getText());

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(InserirEscola.this, "Escola inserida correctament!");

                        if (parent != null) {
                            parent.iniciarCombo(); // Atualiza o JComboBox com a nova escola
                        }
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(InserirEscola.this, "Error en inserir l'escola.");
                }

            }
        });
        panel.add(afegirEscola);

    }

}

