package AssesmentManager;

import AssesmentManager.Components.BotoQuadrat;
import AssesmentManager.Components.ExportModelToPDF;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GenerarInformes extends JFrame {

    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    JPanel panel;

    JComboBox alumneComboBox;

    JDateChooser dataIniciChooser, dataFinalChooser;

    public GenerarInformes() {
        setBounds(80, 120, 830, 240);
        setTitle("Generar Informes");
        iniciarPrograma();
        setVisible(true); // Fes visible la finestra

    }

    public void iniciarPrograma() {
        iniciarPanel();
        iniciarLabels();
        iniciarFields();
        iniciarBotons();
    }

    private void iniciarPanel() {
        panel = new JPanel();
        panel.setLayout(null); // Estableix el layout del panell a null per permetre la posició absoluta
        getContentPane().add(panel); // Afegeix el panell al contenidor de la finestra


    }

    private void iniciarLabels() {

        // <editor-fold> Data Inici Label
        JLabel dataIniciLabel = new JLabel("Data Inici");
        dataIniciLabel.setBounds(127, 0, 150, 30);
        dataIniciLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(dataIniciLabel);

        // </editor-fold>

        // <editor-fold> Data Final Label

        JLabel dataFinalLabel = new JLabel("Data Final");
        dataFinalLabel.setBounds(307, 0, 150, 30);
        dataFinalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(dataFinalLabel);

        // </editor-fold>

        // <editor-fold> Alumne Label

        JLabel alumneLabel = new JLabel("Alumne");
        alumneLabel.setBounds(487, 0, 200, 30);
        alumneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(alumneLabel);


        // </editor-fold>

        // <editor-fold> Línia

        JLabel liniaDivisora1 = new JLabel();
        liniaDivisora1.setBorder(BorderFactory.createLineBorder(new Color(189, 193, 198), 1));
        liniaDivisora1.setBounds(0, 110, 830, 2);
        panel.add(liniaDivisora1);

        // </editor-fold>

    }

    private void iniciarFields() {

        // <editor-fold> Data Inici Date Chooser

        dataIniciChooser = new JDateChooser();
        dataIniciChooser.setBounds(127, 40, 150, 40);
        panel.add(dataIniciChooser);

        // </editor-fold>

        // <editor-fold> Data Final Date Chooser

        dataFinalChooser = new JDateChooser();
        dataFinalChooser.setBounds(307, 40, 150, 40);
        panel.add(dataFinalChooser);

        // </editor-fold>

        // <editor-fold> Alumne ComboBox

        alumneComboBox = new JComboBox();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT Nom FROM Clientes ORDER BY Nom";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Recórrer els resultats i afegir-los al JComboBox
            while (resultSet.next()) {
                String nomClient = resultSet.getString("Nom");
                alumneComboBox.addItem(nomClient);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error en carregar l'alumnes.");
        }
        alumneComboBox.setBounds(487, 40, 200, 40);
        alumneComboBox.setSelectedItem(null);
        panel.add(alumneComboBox);

        // </editor-fold>

    }

    private void iniciarBotons() {

        // <editor-fold> Boto Assessments per Data

        BotoQuadrat assessmentsButton = new BotoQuadrat("Assessments Fets per Data", 10, 140, 250, 40);

        assessmentsButton.addActionListener(e -> {

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sql = "";
                if (dataIniciChooser.getDate() == null || dataFinalChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Si us plau, defineix les dates");

                } else {

                    if (alumneComboBox.getSelectedItem() == null) {
                        sql = "SELECT a.ID, c.Nom, a.cod_assessment AS Cod_Assessment, a.Paid_On, a.Price AS Valor_en_AUD, a.Valor_en_Real FROM assessments AS a " +
                                "JOIN clientes as c ON a.ID_Client = c.ID_Cliente " +
                                "WHERE Paid_On BETWEEN ? AND ? " +
                                "ORDER BY Nom, Paid_on";

                    } else {
                        sql = "SELECT a.ID, c.Nom, a.cod_assessment AS Cod_Assessment, a.Paid_On, a.Price AS Valor_en_AUD, a.Valor_en_Real FROM assessments AS a " +
                                "JOIN clientes as c ON a.ID_Client = c.ID_Cliente " +
                                "WHERE Paid_On BETWEEN ? AND ? AND Nom = ?" +
                                "ORDER BY Nom, Paid_on";
                    }

                    PreparedStatement pstmt = connection.prepareStatement(sql); // Prepara la consulta SQL
                    pstmt.setDate(1, new java.sql.Date(dataIniciChooser.getDate().getTime()));
                    pstmt.setDate(2, new java.sql.Date(dataFinalChooser.getDate().getTime()));

                    if (alumneComboBox.getSelectedItem() != null) {
                        pstmt.setString(3, (String) alumneComboBox.getSelectedItem());
                    }

                    ResultSet rs = pstmt.executeQuery();

                    DefaultTableModel model1 = new DefaultTableModel(new Object[]{"ID", "Nom", "Cod_Assessment", "Data Pago", "Valor_en_AUD", "Valor_en_Real"}, 0);

                    while (rs.next()) {
                        String col1 = rs.getString("ID");
                        String col2 = rs.getString("Nom");
                        String col3 = rs.getString("Cod_Assessment");
                        String col4 = rs.getString("Paid_On");
                        String col5 = "$ " + rs.getString("Valor_en_AUD") + ",00";
                        String col6 = "$ " + rs.getString("Valor_en_Real") + ",00";

                        model1.addRow(new Object[]{col1, col2, col3, col4, col5, col6});
                    }

                    ExportModelToPDF.exportModelToPDF(model1, "C:\\Users\\gusta\\Documents\\Assessments\\Assessments_report.pdf");
                }


            } catch (SQLException d) {
                throw new RuntimeException(d);
            }

        });

        panel.add(assessmentsButton);


        // </editor-fold>

        // <editor-fold> Informe Consolidade de Renda per Mes Button
        BotoQuadrat rendaButton = new BotoQuadrat("Informe Consolidado de Renda per Mes", 280, 140, 250, 40);
        panel.add(rendaButton);

        // </editor-fold>

        // <editor-fold> Informe de Ingressos a Rebre Button
        BotoQuadrat ingressosButton = new BotoQuadrat("Informe de Ingressos a Rebre", 550, 140, 250, 40);
        panel.add(ingressosButton);
        panel.setVisible(true);

        // </editor-fold>

    }

}
