package AssesmentManager.Components;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.property.TextAlignment;

import javax.swing.table.DefaultTableModel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class ExportModelToPDF {

    public static void exportModelToPDF(DefaultTableModel model1, String filePath) {

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Define the font for the PDF
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Add title to the document
            Paragraph title = new Paragraph("Assessments Report").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(16);
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph("\n")); // Adds some spacing

            // Create table with the same number of columns as model1
            Table table = new Table(model1.getColumnCount());

            // Add table headers
            for (int i = 0; i < model1.getColumnCount(); i++) {
                Cell headerCell = new Cell().add(new Paragraph(model1.getColumnName(i)).setFont(font));
                headerCell.setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(headerCell);
            }

            // Add table rows
            for (int row = 0; row < model1.getRowCount(); row++) {
                for (int col = 0; col < model1.getColumnCount(); col++) {
                    Cell cell = new Cell().add(new Paragraph(model1.getValueAt(row, col).toString()).setFont(font));
                    cell.setTextAlignment(TextAlignment.CENTER);
                    table.addCell(cell);
                }
            }

            // Add table to the document
            document.add(table);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

