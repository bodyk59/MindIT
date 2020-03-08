// We can get PDF report for needed table from database from this class.
package employeeDatabase.helpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import employeeDatabase.helpers.ConnectionManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportManager {
    //Static method for creating report with needed parameters
    public static void getReport(AnchorPane anchorPane,
                          String reportName,
                          String title,
                          String nameOfSchemas,
                          String nameOfTable,
                          String[] namesOfColumns,
                          String[] namesOfColumnsFromDatabase){
        try {
            //Create DirectoryChooser(we can create report in needed directory)
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            File file = directoryChooser.showDialog(stage);

            //If file really exist, we do next
            if (file != null) {
                //Set some document parameters
                Document document = new Document(PageSize.A4, 0f, 0f, 0f, 0f);
                //We send report name to this method for creating this report
                PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath() + "/" + reportName + ".pdf"));

                //Open document
                document.open();

                //Set some settings for text from this report
                Font FontForOtherElements = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18f);
                Font FontForTableHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14f);
                Font FontForTable = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9f);

                //Create report title
                Paragraph paragraph = new Paragraph(new Phrase(title, FontForOtherElements));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                //Create field with print-out date
                Paragraph paragraphDate = new Paragraph(new Phrase("\nPrint-out date: " + java.time.LocalDate.now(), FontForTableHeader));
                paragraphDate.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraphDate);

                //Create table with needed length
                PdfPTable pdfPTable = new PdfPTable(namesOfColumns.length);

                //Set some settings for table from this report
                pdfPTable.setWidthPercentage(95.23f);
                pdfPTable.setSpacingBefore(35);
                pdfPTable.setSpacingAfter(25);

                //Create first row with columns name
                for (String namesOfColumn : namesOfColumns) {
                    PdfPCell pdfPCell = new PdfPCell(new Phrase(namesOfColumn, FontForTableHeader));
                    pdfPCell.setRotation(90);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTable.addCell(pdfPCell);
                }

                //Create connection to database and get all information from needed table
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + nameOfSchemas + "." + nameOfTable + ";");
                ResultSet resultSet = preparedStatement.executeQuery();

                //Create rows with information from equivalent rows from database table
                pdfPTable.setHeaderRows(1);
                while (resultSet.next()) {
                    for (String namesOfColumnFromDatabase : namesOfColumnsFromDatabase) {
                        pdfPTable.addCell(new Phrase(Element.ALIGN_MIDDLE, resultSet.getString(namesOfColumnFromDatabase), FontForTable));
                    }
                }

                //Add table to document
                document.add(pdfPTable);
                //Close document
                document.close();
            } else {
                //Show alert if we do not choose directory
                Alert alert = new Alert(Alert.AlertType.WARNING, "You don`t choose any directories!", ButtonType.OK);
                alert.setTitle("Invalid");
                alert.showAndWait();
            }
        } catch (DocumentException | FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}