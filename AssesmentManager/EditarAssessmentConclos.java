package AssesmentManager;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class EditarAssessmentConclos extends JFrame {

    CrearVentana parent;
    JPanel panel;

    String ID;

    JTextField clientField, assessmentcodField, preuField, preuBRLField, obsField;
    JDateChooser vencimentChooser;


    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    public EditarAssessmentConclos(CrearVentana parent, String ID, String client, String code, String conclusionDate, String priceAUD, String priceBRL, String Obs) {
        this.parent = parent;


        setBounds(80, 120, 500, 500); // Estableix la posició i la mida de la finestra
        setTitle("Editar Assessment"); // Estableix el títol de la finestra
        iniciarPrograma(); // Crida la funció iniciarPrograma que iniciarà tots els components de la finestra

        // Assignar els valors passats als camps
        this.ID = ID;
        clientField.setText(client);
        assessmentcodField.setText(code);
        preuField.setText(priceAUD);
        preuBRLField.setText(priceBRL);
        obsField.setText(Obs);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(conclusionDate);
            vencimentChooser.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void iniciarPrograma() {
        iniciarPanel();
        iniciarLabels();
        iniciarCampsDeText();
        iniciarDateChooser();
        iniciarBotons();

    }

    public void iniciarPanel() {
        panel = new JPanel(); // Crea un nou objecte JPanel
        panel.setLayout(null); // Estableix el layout del panell a null per permetre la posició absoluta
        getContentPane().add(panel); // Afegeix el panell al contenidor de la finestra
    }

    public void iniciarLabels() {
        addLabel("Client", 20, 10, 160, 40);
        addLabel("Data de Conclusió", 20, 60, 160, 40);
        addLabel("Codi d'Assessment", 20, 110, 160, 40);
        addLabel("PreuAUD", 20, 160, 160, 40);
        addLabel("PreuBRL", 20, 210, 160, 40);
        addLabel("Obs", 20, 260, 160, 40);
    }

    public void addLabel(String text, int x, int y, int width, int height) {
        JLabel item = new JLabel(text); // Crea una nova etiqueta amb el text donat
        item.setBounds(x, y, width, height); // Estableix la posició i mida de l'etiqueta
        panel.add(item); // Afegeix l'etiqueta al panell
    }

    public void iniciarCampsDeText() {
        clientField = addCamp(180, 10, 160, 40);
        assessmentcodField = addCamp(180, 110, 160, 40);
        preuField = addCamp(180, 160, 160, 40);
        preuBRLField = addCamp(180, 210, 160, 40);
        obsField = addCamp(180, 260, 160, 40);
    }

    // Mètode per crear i afegir un camp de text al panell
    public JTextField addCamp(int x, int y, int width, int height) {
        JTextField camp = new JTextField(); // Crea un nou camp de text
        camp.setBounds(x, y, width, height); // Estableix la posició i mida del camp de text
        panel.add(camp); // Afegeix el camp de text al panell
        return camp; // Retorna el camp de text creat
    }

    // Inicialitza el selector de dates
    public void iniciarDateChooser() {
        vencimentChooser = new JDateChooser(); // Crea un nou objecte JDateChooser
        vencimentChooser.setBounds(180, 60, 160, 40); // Estableix la posició i mida del selector de dates
        panel.add(vencimentChooser); // Afegeix el selector de dates al panell
        vencimentChooser.getDateEditor().setEnabled(false);
    }

    public void iniciarBotons() {
        JButton editarButton = new JButton("Actualitzar"); // Crea un nou botó amb el text "Inserir"
        editarButton.setBounds(180, 360, 160, 40); // Estableix la posició i mida del botó
        editarButton.addActionListener(new ActionListener() { // Afegeix un ActionListener al botó
            @Override
            public void actionPerformed(ActionEvent e) {
                actualitzarDades(); // Crida el mètode inserirDades quan es prem el botó
            }
        });
        panel.add(editarButton); // Afegeix el botó al panell
    }

    public void actualitzarDades() {

        try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
            String sql = "UPDATE assessments SET Client = ?, Conclusion_Date = ?, Price = ?, Cod_Assessment = ?, Valor_en_real = ?, Obs = ? WHERE ID = ?;"; // Defineix la consulta SQL per inserir dades
            PreparedStatement pstmt = connection.prepareStatement(sql); // Prepara la consulta SQL

            pstmt.setString(1, clientField.getText());
            pstmt.setDate(2, new java.sql.Date(vencimentChooser.getDate().getTime()));
            pstmt.setInt(3, Integer.parseInt(preuField.getText()));
            pstmt.setString(4, assessmentcodField.getText());
            if (!preuBRLField.getText().trim().isEmpty()) {
                pstmt.setString(5, preuBRLField.getText());
            } else {
                pstmt.setNull(5, java.sql.Types.DECIMAL);
            }

            pstmt.setString(6, obsField.getText());


            pstmt.setString(7, ID);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Assessment actualitzat correctament!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        parent.model1.setRowCount(0);
        parent.donarValorTaulaAssessmentOberts();
    }

}