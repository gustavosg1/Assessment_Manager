package AssesmentManager;

import javax.swing.*; //Jframe
import java.awt.event.ActionEvent; // esdeveniments per als botons
import java.awt.event.ActionListener; // esdeveniments per als botons
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.HashMap;

import AssesmentManager.Components.BotoQuadrat;
import AssesmentManager.Components.RoundedPanel;
import org.json.JSONObject;
import AssesmentManager.Components.ConsultaAPICanvi;

import static AssesmentManager.GenerarInformes.*;

public class CrearVentana extends JFrame {
    JPanel panel1; // inicia panel de la finestra principal que és utilitzat en més d'un metode
    JPanel panel2;

    DefaultTableModel model1; // model taula de assessments per fer
    DefaultTableModel model2; // model taula de assessments per pagar

    String url = "jdbc:mysql://127.0.0.1:3306/assessment_management"; // URL de la base de dades
    String username = "root"; // Nom d'usuari de la base de dades
    String password = "root"; // Contrasenya de la base de dades

    HashMap escolaMap = new HashMap<>();
    HashMap clientMap = new HashMap<>();

    BotoQuadrat EUR, BRL;
    RoundedPanel cercleCanvi;

    double BRLExchangeRate = new ConsultaAPICanvi().BRLCanvi();
    double EURExchangeRate = new ConsultaAPICanvi().EURCanvi();

    Border lineBorder = BorderFactory.createLineBorder(new Color(189, 193, 198), 1);

    public CrearVentana() {
        setDefaultCloseOperation(EXIT_ON_CLOSE); //tanca programa quan tanqui finestra
        setExtendedState(JFrame.MAXIMIZED_BOTH); // obre la finestra maximitzada
        setTitle("Assessment Manager"); // defineix el titol de la finestra
        iniciarPrograma(); // crida la funció iniciarPrograma que iniciarà tots els components de la finestra
        setLayout(new BorderLayout());
    }

    public void iniciarPrograma() {
        iniciarPanel();
        iniciarLabelsCentrals();
        iniciarLabelsLaterals();
        iniciarBotonsCentrals();
        iniciarBotonsLaterals();
        iniciarTaulaAssessmentOberts();
        iniciarTaulaAsseessmentNoPagats();

    }

    public void iniciarPanel() {

        // <editor-fold> Panel 1
        panel1 = new JPanel();
        panel1.setLayout(null); // neteja el layout pre definit
        panel1.setBounds(260, 0, 1106, 697);
        panel1.setBorder(lineBorder);
        getContentPane().add(panel1, BorderLayout.CENTER); // afegeix el panel en la finestra
        // </editor-fold>

        // <editor-fold> Panel 2
        panel2 = new JPanel();
        panel2.setLayout(null); // neteja el layout pre definit
        panel2.setBounds(0, 0, 260, 697);
        panel2.setBackground(new Color(228, 230, 235));
        panel2.setBorder(lineBorder);
        getContentPane().add(panel2, BorderLayout.CENTER); // afegeix el panel en la finestra

        // </editor-fold>

    }

    public void iniciarLabelsCentrals() {

        // <editor-fold> Label Assessments Oberts
        JLabel assessmentsOberts = new JLabel("Assessments Oberts");
        assessmentsOberts.setBounds(80, 20, 900, 40);
        assessmentsOberts.setHorizontalAlignment(SwingConstants.CENTER);
        assessmentsOberts.setFont(new Font("Merriweather", Font.BOLD, 26));
        panel1.add(assessmentsOberts);

        // </editor-fold>

        // <editor-fold> Label Assessments A Pagar
        JLabel assessmentsAPagar = new JLabel("Assessments A Pagar");
        assessmentsAPagar.setBounds(80, 380, 900, 40);
        assessmentsAPagar.setHorizontalAlignment(SwingConstants.CENTER);
        assessmentsAPagar.setFont(new Font("Merriweather", Font.BOLD, 26));
        panel1.add(assessmentsAPagar);
        // </editor-fold>

    }

    public void iniciarLabelsLaterals() {

        // <editor-fold> Label Inserir Assessment
        ImageIcon nouAssessmentIconOriginal = new ImageIcon("AssesmentManager/Components/Nou-Assessment_Icon.png");
        Image nouAssessmentIconRedm = nouAssessmentIconOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon nouAssessmentIcon = new ImageIcon(nouAssessmentIconRedm);

        JLabel inserirAssessment = new JLabel("Inserir Nou Assessment", nouAssessmentIcon, JLabel.LEFT); //crear botó
        inserirAssessment.setBounds(10, 10, 240, 40);

        labelsLateralsFeatures(inserirAssessment);

        inserirAssessment.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                InserirAssessment v2 = new InserirAssessment(CrearVentana.this);
                v2.setVisible(true);
            }
        });

        panel2.add(inserirAssessment); // inserir botó en el panel

        // </editor-fold>

        // <editor-fold> Label Inserir Escola
        ImageIcon novaEscolaIconOriginal = new ImageIcon("AssesmentManager/Components/Nova_Escola_Icon.png");
        Image novaEscolaIconRedm = novaEscolaIconOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon novaEscolaIcon = new ImageIcon(novaEscolaIconRedm);

        JLabel inserirEscola = new JLabel("Inserir Nova Escola", novaEscolaIcon, JLabel.LEFT); //crear botó
        inserirEscola.setBounds(10, 60, 240, 40);

        labelsLateralsFeatures(inserirEscola);

        inserirEscola.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                InserirEscola v5 = new InserirEscola();
                v5.setVisible(true);
            }
        });

        panel2.add(inserirEscola);

        // </editor-fold>

        // <editor-fold> Label Inserir Alumne
        ImageIcon nouAlumneIconOriginal = new ImageIcon("AssesmentManager/Components/Nou_Alumne_Icon.png");

        Image nouAlumneIconRedm = nouAlumneIconOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon nouAlumneIcon = new ImageIcon(nouAlumneIconRedm);

        JLabel inserirAlumne = new JLabel("Inserir Nou Alumne", nouAlumneIcon, JLabel.LEFT); //crear botó
        inserirAlumne.setBounds(10, 110, 240, 40);

        labelsLateralsFeatures(inserirAlumne);

        inserirAlumne.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                InserirAlumne v6 = new InserirAlumne(CrearVentana.this);
                v6.setVisible(true);
            }
        });

        panel2.add(inserirAlumne);

        // </editor-fold>

        // <editor-fold> Label Consultar Alumne

        ImageIcon consultarAlumneOriginal = new ImageIcon("AssesmentManager/Components/Consultar_Alumno_Icon.png");
        Image consultarAlumneRedm = consultarAlumneOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon consultarAlumneIcon = new ImageIcon(consultarAlumneRedm);

        JLabel consultarAlumne = new JLabel("Consultar Alumne", consultarAlumneIcon, JLabel.LEFT); //crear botó
        consultarAlumne.setBounds(10, 160, 240, 40);

        labelsLateralsFeatures(consultarAlumne);

        consultarAlumne.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ConsultarAlumne v7 = new ConsultarAlumne();
            }
        });

        panel2.add(consultarAlumne); // inserir botó en el panel

        // </editor-fold>

        // <editor-fold> Label Consultar Escola

        ImageIcon consultarEscolaOriginal = new ImageIcon("AssesmentManager/Components/Consultar_Escola_Icon.png");
        Image consultarEscolaRedm = consultarEscolaOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon consultarEscolaIcon = new ImageIcon(consultarEscolaRedm);

        JLabel consultarEscola = new JLabel("Consultar Escola", consultarEscolaIcon, JLabel.LEFT); //crear botó
        consultarEscola.setBounds(10, 210, 240, 40);

        labelsLateralsFeatures(consultarEscola);

        consultarEscola.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ConsultarEscola v7 = new ConsultarEscola();
            }
        });


        panel2.add(consultarEscola); // inserir botó en el panel

        // </editor-fold>

        // <editor-fold> Línia

        JLabel liniaDivisora1 = new JLabel();
        liniaDivisora1.setBorder(lineBorder);
        liniaDivisora1.setBounds(0, 260, 260, 2);
        panel2.add(liniaDivisora1);

        // </editor-fold>

        // <editor-fold> Generar Informes

        ImageIcon generarInformeOriginal = new ImageIcon("AssesmentManager/Components/Generar_Informes_Icon.png");
        Image generarInformeRedm = generarInformeOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon generarInformeIcon = new ImageIcon(generarInformeRedm);

        JLabel generarInformes = new JLabel("Generar Informes", generarInformeIcon, JLabel.LEFT); //crear botó
        generarInformes.setBounds(10, 270, 240, 40);

        labelsLateralsFeatures(generarInformes);

        generarInformes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GenerarInformes v9 = new GenerarInformes();
            }
        });

        panel2.add(generarInformes);

        // </editor-fold>

        // <editor-fold> Línia

        JLabel liniaDivisora2 = new JLabel();
        liniaDivisora2.setBorder(lineBorder);
        liniaDivisora2.setBounds(0, 320, 260, 2);
        panel2.add(liniaDivisora2);

        // </editor-fold>

        // <editor-fold> Label Canvi

        cercleCanvi = new RoundedPanel();
        cercleCanvi.setBounds(28, 450, 200, 200);
        cercleCanvi.setText(String.valueOf(BRLExchangeRate));
        panel2.add(cercleCanvi);

        // </editor-fold>

    }

    private void iniciarBotonsLaterals() {

        // <editor-fold> Boto BRL
        BRL = new BotoQuadrat("BRL", 60, 400, 60, 30);
        BRL.setBorder(BorderFactory.createBevelBorder(1));
        panel2.add(BRL);

        BRL.addActionListener(d -> {
            cercleCanvi.setText(String.valueOf(BRLExchangeRate));
        });

        BRL.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                BRL.setBorder(BorderFactory.createBevelBorder(1)); // Pressed effect
                EUR.setBorder(BorderFactory.createBevelBorder(0));

            }

            public void mouseReleased(MouseEvent e) {
                BRL.setBorder(BorderFactory.createBevelBorder(1)); // Pressed effect

            }
        });

        // </editor-fold>

        // <editor-fold> Boto EUR
        EUR = new BotoQuadrat("EUR", 140, 400, 60, 30);
        panel2.add(EUR);

        EUR.addActionListener(d -> {
            cercleCanvi.setText(String.valueOf(EURExchangeRate));
        });

        EUR.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EUR.setBorder(BorderFactory.createBevelBorder(1)); // Pressed effect
                BRL.setBorder(BorderFactory.createBevelBorder(0));
            }

            public void mouseReleased(MouseEvent e) {
                EUR.setBorder(BorderFactory.createBevelBorder(1)); // Pressed effect
            }
        });
        // </editor-fold>
    }

    public void labelsLateralsFeatures(JLabel label) {
        label.setIconTextGap(10);
        label.setOpaque(true);
        label.setBackground(new Color(228, 230, 235));
        label.setFont(new Font("Noto Sans", Font.PLAIN, 12));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(208, 213, 219)); // Muda a cor quando o mouse entra
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(new Color(228, 230, 235)); // Volta à cor original quando o mouse sai
            }

        });
    }

    public void iniciarBotonsCentrals() {

        // <editor-fold> Botó Concluir Assessemnt
        BotoQuadrat concloureAssessment = new BotoQuadrat("Marcar com a Conclòs", 300, 280, 200, 40);

        // crear acció per al botó marcar el assessment sel·leccionat com conclòs
        ActionListener marcarAssessementComConclos = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < model1.getRowCount(); i++) {

                    if (Boolean.TRUE.equals(model1.getValueAt(i, 8))) {
                        try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
                            String sql = "UPDATE assessments SET conclusion_date = ?, Obs = '' WHERE ID = ?;"; // Defineix la consulta SQL per inserir dades
                            PreparedStatement pstmt = connection.prepareStatement(sql); // Prepara la consulta SQL

                            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                            pstmt.setInt(2, Integer.parseInt((String) model1.getValueAt(i, 0)));

                            pstmt.executeUpdate();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                }

                model1.setRowCount(0);
                donarValorTaulaAssessmentOberts();
                model2.setRowCount(0);
                donarValorTaulaAsseessmentNoPagats();
            }
        };

        panel1.add(concloureAssessment); // inserir botó en el panel
        concloureAssessment.addActionListener(marcarAssessementComConclos); // inserir acció en el botó

// </editor-fold>

        // <editor-fold> Botó Editar Assessment obert
        BotoQuadrat editarAssessment = new BotoQuadrat("Editar Assessment Obert", 520, 280, 200, 40); // crear botó

        ActionListener ferModificacions = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < model1.getRowCount(); i++) {

                    if (Boolean.TRUE.equals(model1.getValueAt(i, 8))) {

                        String ID = (String) model1.getValueAt(i, 0);
                        String client = (String) model1.getValueAt(i, 1);
                        String dueDate = (String) model1.getValueAt(i, 2);
                        String price = (String) model1.getValueAt(i, 3);
                        String assessmentCode = (String) model1.getValueAt(i, 4);
                        String school = (String) model1.getValueAt(i, 5);
                        String obs = (String) model1.getValueAt(i, 6);

                        EditarAssessment v3 = new EditarAssessment(CrearVentana.this, ID, client, dueDate, price, assessmentCode, school, obs);
                        v3.setVisible(true);

                    }
                }
            }
        };

        editarAssessment.addActionListener(ferModificacions);
        panel1.add(editarAssessment); // inserir botó en el panel

        // </editor-fold>

        // <editor-fold> "Botó Pagar Assessemnt"


        BotoQuadrat pagarAssessment = new BotoQuadrat("Marcar com a Pagat", 300, 640, 200, 40); // crear botó

        // crear acció per al botó marcar el assessment sel·leccionat com conclòs
        ActionListener marcarAssessementComPagat = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Nombre de files en la taula: " + model2.getRowCount());

                for (int i = 0; i < model2.getRowCount(); i++) { //revisar totes les files

                    if (Boolean.TRUE.equals(model2.getValueAt(i, 7))) { // si la fila 8 conté true

                        System.out.println("Actualitzant assessment amb ID: " + model2.getValueAt(i, 0)); // Missatge per depuració

                        try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
                            String sql = "UPDATE assessments SET Paid_On = ?, Obs = '' WHERE ID = ?;"; // Defineix la consulta SQL per inserir dades
                            PreparedStatement pstmt = connection.prepareStatement(sql); // Prepara la consulta SQL

                            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now())); //assigna la data actual
                            pstmt.setInt(2, Integer.parseInt((String) model2.getValueAt(i, 0))); //identifica el ID que ha d'editar

                            pstmt.executeUpdate(); //executa

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                }

                model2.setRowCount(0); //neteja el model
                donarValorTaulaAsseessmentNoPagats(); //dona valors un'altra vegada a la taula
            }
        };

        panel1.add(pagarAssessment); // inserir botó en el panel
        pagarAssessment.addActionListener(marcarAssessementComPagat); // inserir acció en el botó

        // </editor-fold>//

        // <editor-fold> Botó Editar Assessemnt conclòs

        BotoQuadrat editarAssessmentConclos = new BotoQuadrat("Editar Assessment Pagat", 520, 640, 200, 40); // crear botó

        ActionListener ferModificacionsAssessmentConclos = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < model2.getRowCount(); i++) {

                    if (Boolean.TRUE.equals(model2.getValueAt(i, 7))) {

                        String ID = (String) model2.getValueAt(i, 0);
                        String client = (String) model2.getValueAt(i, 1);
                        String code = (String) model2.getValueAt(i, 2);
                        String conclusionDate = (String) model2.getValueAt(i, 3);
                        String priceAUD = (String) model2.getValueAt(i, 4);
                        String priceBRL = (String) model2.getValueAt(i, 5);
                        String Obs = (String) model2.getValueAt(i, 6);

                        EditarAssessmentConclos v4 = new EditarAssessmentConclos(CrearVentana.this, ID, client, code, conclusionDate, priceAUD, priceBRL, Obs);
                        v4.setVisible(true);

                    }
                }
            }
        };

        panel1.add(editarAssessmentConclos);
        editarAssessmentConclos.addActionListener(ferModificacionsAssessmentConclos);
        // </editor-fold>

    }

    public void iniciarTaulaAssessmentOberts() {

        // crear el model
        model1 = new DefaultTableModel(new Object[]{"Ass Num", "Client", "Data", "Preu", "Codi", " Escola", "Obs", "Status", "Sel·leccionar"}, 0) { //crear el model de la taula
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 8:
                        return Boolean.class; // La 9ª columna té caixes de sel·lecció
                    default:
                        return String.class; // Totes l'altres son string
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // La columna 9 és editable
            }
        };


        // crear la taula
        JTable table1 = new JTable(model1);
        /*    @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // Verificar se a coluna de seleção (coluna 8) está marcada
                Boolean isChecked = (Boolean) getModel().getValueAt(row, 8);
                if (isChecked != null && isChecked) {
                    c.setBackground(new Color(191, 214, 237)); // Cor de fundo quando a linha está marcada
                } else {
                    c.setBackground(Color.WHITE); // Cor de fundo padrão
                }

                return c;
            }
        };

        model1.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Repaint apenas quando o valor de uma célula for atualizado
                if (e.getColumn() == 8) { // Somente reagir às mudanças na coluna de checkbox
                    table1.repaint(); // Forçar a repintura
                }
            }
        });*/

        // crear padding (vora interior)

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Define padding para as células
                label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3)); // 10px de padding em todos os lados

                return label;
            }
        };

        for (int i = 0; i < table1.getColumnCount() - 1; i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        ;

        // altres caracterisques de la taula
//        table1.getColumnModel().getColumn(7).setCellRenderer(new StatusColumnRenderer());
        table1.setFillsViewportHeight(true); // col·locar les caixes de sel·lecció
        table1.setGridColor(Color.LIGHT_GRAY); // Cor da linha separadora
        table1.setShowVerticalLines(false); // Ocultar as linhas verticais para um design mais limpo
        JScrollPane scrollPane1 = new JScrollPane(table1); // crear el scroll panel i inserir-li la taula

        scrollPane1.setBounds(80, 80, 900, 180);
        panel1.add(scrollPane1); // afegir scrollPanel en el panel


        donarValorTaulaAssessmentOberts();

    }

//        // Classe interna que define o renderizador personalizado para a coluna de Status
//    private class StatusColumnRenderer extends DefaultTableCellRenderer {
//            private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//            ImageIcon bolitaVermellha = new ImageIcon("AssesmentManager/Components/bolinha_vermelha.png");
//            Image bolitaVermellaRedm = bolitaVermellha.getImage().getScaledInstance(4,4,Image.SCALE_SMOOTH);
//            ImageIcon bolitaVermellaIcon = new ImageIcon (bolitaVermellaRedm);
//
//            ImageIcon bolitaAmarilla = new ImageIcon("AssesmentManager/Components/bolinha_amarela.png");
//            Image bolitaAmarillaRedm = bolitaAmarilla.getImage().getScaledInstance(4,4,Image.SCALE_SMOOTH);
//            ImageIcon bolitaAmarillaIcon = new ImageIcon (bolitaAmarillaRedm);
//
//            ImageIcon bolitaVerde = new ImageIcon("AssesmentManager/Components/bolinha_verde.png");
//            Image bollitaVerdeRedm = bolitaVerde.getImage().getScaledInstance(4,4,Image.SCALE_SMOOTH);
//            ImageIcon bolitaVerdeIcon = new ImageIcon (bollitaVerdeRedm);
//
//
//            @Override
//            protected void setValue(Object value) {
//
//                if (value == null || !(value instanceof String)) {
//                    setIcon(null); // Não exibir nenhum ícone
//                    setBackground(Color.WHITE); // Fundo padrão
//                    setText("");
//                    return;
//                }
//
//                // Get the current date for comparison
//                Date currentDate = new Date();
//
//                try {
//                    // Parse the date from the table's "Data" column
//                    String dateString = (String) value;
//                    Date date = dateFormat.parse(dateString);
//
//                    // Determine the status color based on the date
//                    long diff = date.getTime() - currentDate.getTime();
//                    long daysUntilDue = diff / (1000 * 60 * 60 * 24);
//
//                    if (daysUntilDue < 0) {
//                        // If the date is in the past, set background to red (overdue)
//                        setBackground(Color.RED);
//                    } else if (daysUntilDue <= 1) {
//                        // If the date is today or tomorrow, set background to yellow (due soon)
//                        setBackground(Color.YELLOW);
//                    } else {
//                        // If the date is further in the future, set background to green (not due soon)
//                        setBackground(Color.GREEN);
//                    }
//                } catch (ParseException e) {
//                    // If parsing fails, set the background to white (default)
//                    setBackground(Color.WHITE);
//                }
//
//                // Set the text to empty since we're using background color as an indicator
//                setText("");
//            }
//        }

    public void donarValorTaulaAssessmentOberts() {

        try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
            String sql = "SELECT a.ID, c.Nom, a.Due_Date, a.Price, a.Cod_Assessment, e.Escola, a.Obs, e.ID_Escola, c.ID_Cliente FROM assessments as a " +
                    "JOIN clientes c ON a.ID_Client = c.ID_Cliente " +
                    "JOIN escola e ON a.ID_School = e.ID_Escola " +
                    "WHERE conclusion_date is null " +
                    "ORDER BY Due_Date;"; // Defineix la consulta SQL per inserir dades
            PreparedStatement pstmt = connection.prepareStatement(sql); // Prepara la consulta SQL
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String col1 = rs.getString("ID");
                String col2 = rs.getString("Nom");
                String col3 = rs.getString("Due_Date");
                String col4 = rs.getString("Price");
                String col5 = rs.getString("Cod_Assessment");
                String col6 = rs.getString("Escola");
                String col7 = rs.getString("Obs");
                model1.addRow(new Object[]{col1, col2, col3, col4, col5, col6, col7});

                escolaMap.put(col6, rs.getInt("ID_Escola"));
                clientMap.put(col2, rs.getInt("ID_Cliente"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void iniciarTaulaAsseessmentNoPagats() {

        //crear el model

        model2 = new DefaultTableModel(new Object[]{"ID", "Client", "Code", "Conclusion Date", "Price AUD", "Price BRL", "Obs", "Sel·leccionar"}, 0) { //crear el model de la taula
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 7:
                        return Boolean.class; // La 8ª columna té caixes de sel·lecció
                    default:
                        return String.class; // Totes l'altres son string
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // La columna 8 és editable
            }
        };

        // crear la taula
        JTable table2 = new JTable(model2);

        // crear la vora interior

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Define padding para as células
                label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3)); // 10px de padding em todos os lados

                return label;
            }
        };

        for (int i = 0; i < table2.getColumnCount() - 1; i++) {
            table2.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        ;


        table2.setFillsViewportHeight(true); // col·locar les caixes de sel·lecció
        JScrollPane scrollPane2 = new JScrollPane(table2); // crear el scroll panel i inserir-li la taula

        scrollPane2.setBounds(80, 440, 900, 180);
        panel1.add(scrollPane2); // afegir scrollPanel en el panel

        donarValorTaulaAsseessmentNoPagats();
    }

    public void donarValorTaulaAsseessmentNoPagats() {

        try (Connection connection = DriverManager.getConnection(url, username, password)) { // Crea una connexió amb la base de dades
            String sql = "SELECT a.ID, c.Nom, a.Due_Date, a.Cod_Assessment, a.Conclusion_Date, a.Price, a.Valor_en_Real, a.Obs FROM assessments as a " +
                    "JOIN clientes c ON a.ID_Client = c.ID_Cliente " +
                    "WHERE Paid_on is null AND Conclusion_Date is not null " +
                    "ORDER BY Due_Date"; // Defineix la consulta SQL per inserir dades

            PreparedStatement pstmt = connection.prepareStatement(sql); // Prepara la consulta SQL
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String col1 = rs.getString("ID");
                String col2 = rs.getString("Nom");
                String col3 = rs.getString("Cod_Assessment");
                String col4 = rs.getString("Conclusion_Date");
                String col5 = rs.getString("Price");
                String col6 = rs.getString("Valor_en_Real");
                String col7 = rs.getString("Obs");
                model2.addRow(new Object[]{col1, col2, col3, col4, col5, col6, col7});
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }


}

