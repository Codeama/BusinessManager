/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.manager;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author BUKOLA
 */
public class Document_Creation {
    public static void main(String[] args) throws IOException{
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        //doc.save("C:\\Users\\BUKOLA\\Desktop\\pagedFile.pdf");
        //File file = new File("C:\\Users\\BUKOLA\\Desktop\\pagedFile.pdf");
        //PDDocument textPage = PDDocument.load(file);
        //PDPage newPage = doc.getPage(0);
        PDPageContentStream content = new PDPageContentStream(doc, page);
        
        content.setFont(PDType1Font.COURIER, 12);
        content.moveTo(10, 10);
        content.lineTo(700, 10);
        content.stroke();
        //content.drawLine(25, 10, 700, 10);
        content.beginText();
        content.newLineAtOffset(25, 700);
        String text = "Hello, PDF world! :-)";
        content.showText(text);
        content.endText();
        content.close();
        doc.save("C:\\Users\\BUKOLA\\Desktop\\second_file.pdf");
        doc.close();
    }
    
}
