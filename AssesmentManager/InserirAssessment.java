package AssesmentManager;

import javax.swing.*;

import AssesmentManager.Components.BotoQuadrat;
import com.toedter.calendar.JDateChooser; // Classe per posar imatge al calendari

import java.awt.*;
import java.awt.event.ActionEvent; // Importa ActionEvent per gestionar esdeveniments
import java.awt.event.ActionListener; // Importa ActionListener per escoltar esdeveniments
import java.io.*;
import java.sql.*;
import java.text.ParseException; // Classe per gestionar errors de parsing
import java.text.SimpleDateFormat; // Classe per formatar dates
import java.util.Date; // Classe per treballar amb dates
import java.util.HashMap;


public class InserirAssessment extends JFrame {
    CrearVentana parent;
    JPanel panel; // Declara un objecte JPanel per contenir els components de la finestra


    // Dades de connexió a la base de dades
    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    // Camps de text i components de selecció de data i escola
    JTextField preuField, assessmentcodField, schoolField, fileField, obsField;
    JDateChooser vencimentChooser;
    File selectedFile;
    JComboBox escolaComboBox, clientComboBox;
    HashMap escolaMap, clientMap;

    // Constructor de la classe
    public InserirAssessment(CrearVentana parent) {
        this.parent = parent;
        setBounds(80, 120, 500, 500); // Estableix la posició i la mida de la finestra
        setTitle("Inserir Assessment"); // Estableix el títol de la finestra
        iniciarPrograma(); // Crida la funció iniciarPrograma que iniciarà tots els components de la finestra
    }

    // Inicialitza el programa
    public void iniciarPrograma() {
        iniciarPanel();
        iniciarLabels();
        iniciarCampsDeText();
        iniciarDateChooser();
        iniciarBotons();
        iniciarCombo();
    }

    // Inicialitza el panell
    public void iniciarPanel() {
        panel = new JPanel(); // Crea un nou objecte JPanel
        panel.setLayout(null); // Estableix el layout del panell a null per permetre la posició absoluta
        getContentPane().add(panel); // Afegeix el panell al contenidor de la finestra
    }

    // Inicialitza les etiquetes
    public void iniciarLabels() {
        addLabel("Client", 20, 10, 160, 40);
        addLabel("Data de Venciment", 20, 60, 160, 40);
        addLabel("Preu", 20, 110, 160, 40);
        addLabel("Codi d'Assessment", 20, 160, 160, 40);
        addLabel("Escola", 20, 210, 160, 40);
        addLabel("Arxiu", 20, 260, 160, 40);
        addLabel("Observacció", 20, 310, 160, 40);
    }

    // Mètode per afegir una etiqueta al panell
    public void addLabel(String text, int x, int y, int width, int height) {
        JLabel item = new JLabel(text); // Crea una nova etiqueta amb el text donat
        item.setBounds(x, y, width, height); // Estableix la posició i mida de l'etiqueta
        panel.add(item); // Afegeix l'etiqueta al panell
    }

    // Inicialitza els camps de text
    public void iniciarCampsDeText() {
        preuField = addCamp(180, 110, 160, 40);
        assessmentcodField = addCamp(180, 160, 160, 40);
        obsField = addCamp(180, 310, 160, 40);
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

    // Inicialitza el selector de escoles

    public void iniciarCombo() {

        if (escolaComboBox == null) {
            escolaComboBox = new JComboBox();
            escolaComboBox.setBounds(180, 210, 160, 40);
            escolaComboBox.removeAllItems();
        } else {
            escolaComboBox.removeAllItems(); // Limpa as opções, mantendo o mesmo ComboBox
        }

        escolaMap = new HashMap<>(); // Inicialitza el HashMap per emmagatzemar els noms i IDs de les escoles

        // Connexió a la base de dades
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT ID_Escola, Escola FROM Escola ORDER BY Escola";
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

        if (clientComboBox == null) {
            clientComboBox = new JComboBox();
            clientComboBox.setBounds(180, 10, 160, 40);
            clientComboBox.removeAllItems();
        } else {
            clientComboBox.removeAllItems(); // Limpa as opções, mantendo o mesmo ComboBox
        }


        clientMap = new HashMap<>(); // Inicialitza el HashMap per emmagatzemar els noms i IDs de les clients

        // Connexió a la base de dades
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT ID_Cliente, Nom FROM Clientes ORDER BY Nom";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Recórrer els resultats i afegir-los al JComboBox
            while (resultSet.next()) {
                int idClient = resultSet.getInt("ID_Cliente");
                String nomClient = resultSet.getString("Nom");
                clientComboBox.addItem(nomClient);
                clientMap.put(nomClient, idClient); // Emmagatzema l'ID associat amb el nom de l'escola
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en carregar l'alumnes.");
        }

        clientComboBox.setSelectedItem(null);
        panel.add(clientComboBox);

    }

    // Inicialitza els botons
    public void iniciarBotons() {

        // Inicialitza botó 'inserir'

        BotoQuadrat inserirButton = new BotoQuadrat("Inserir", 180, 360, 160, 40); // Crea un nou botó amb el text "Inserir"
        inserirButton.addActionListener(new ActionListener() { // Afegeix un ActionListener al botó
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirDades(); // Crida el mètode inserirDades quan es prem el botó
            }
        });

        panel.add(inserirButton); // Afegeix el botó al panell

        JButton arxiuButton = new JButton("Sel·leccionar Arxiu");
        arxiuButton.setBounds(180, 260, 160, 40);
        arxiuButton.addActionListener(new ActionListener() { // Afegeix un ActionListener al botó
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarArxiu(); // Crida el mètode inserirDades quan es prem el botó
            }
        });
        panel.add(arxiuButton); // Afegeix el botó al panell

        // Inicialitza botó 'Nou Alumne'

        BotoQuadrat nouAlumne = new BotoQuadrat("Nou Alumne", 370, 15, 80, 30);
        nouAlumne.setMargin(new Insets(0, 0, 0, 0));
        nouAlumne.setFont(new Font("Arial", Font.BOLD, 9));
        panel.add(nouAlumne);

        ActionListener inserirNouAlumne = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InserirAlumne v6 = new InserirAlumne(InserirAssessment.this);
                v6.setVisible(true);
            }
        };

        nouAlumne.addActionListener(inserirNouAlumne);
    }

    public void seleccionarArxiu() {
        JFileChooser fileChooser = new JFileChooser(); // Crea un nou objecte JFileChooser
        int result = fileChooser.showOpenDialog(this); // Mostra el diàleg de selecció de fitxers
        if (result == JFileChooser.APPROVE_OPTION) { // Si l'usuari ha seleccionat un fitxer
            selectedFile = fileChooser.getSelectedFile(); // Obté el fitxer seleccionat
        }

    }

    // Mètode per inserir dades a la base de dades
    public void inserirDades() {

        try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
            String sql = "INSERT INTO assessments (Id_client, due_date, price, cod_assessment, ID_school, assessment_document, obs) VALUES (?, ?, ?, ?, ?, ?,?)"; // Defineix la consulta SQL per inserir dades
            PreparedStatement statement = connection.prepareStatement(sql); // Prepara la consulta SQL

            statement.setInt(1, (int) clientMap.get((String) clientComboBox.getSelectedItem())); // Estableix el valor del camp "client"
            statement.setDate(2, new java.sql.Date(vencimentChooser.getDate().getTime()));
            statement.setFloat(3, Float.parseFloat(preuField.getText()));
            statement.setString(4, assessmentcodField.getText());
            statement.setInt(5, (int) escolaMap.get((String) escolaComboBox.getSelectedItem()));


            // Gestió de l'InputStream dins d'un bloc try-with-resources separat
            if (selectedFile != null) {
                try (InputStream input = new FileInputStream(selectedFile)) {
                    statement.setBinaryStream(6, input);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(this, "Fitxer no trobat: " + e.getMessage());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al llegir el fitxer: " + e.getMessage());
                }
            } else {
                statement.setString(6, "Null");
            }

            statement.setString(7, obsField.getText());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Assessment inserit correctament!");
            } else {
                JOptionPane.showMessageDialog(null, "Si us plau, omple tots els camps correctament"); // Ação ao clicar
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en inserir l'assessment.");
        }

        parent.model1.setRowCount(0);
        parent.donarValorTaulaAssessmentOberts();

    }

}