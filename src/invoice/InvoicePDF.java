/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Element;
import rst.pdfbox.layout.elements.Frame;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.VerticalSpacer;
import rst.pdfbox.layout.elements.render.ColumnLayout;
import rst.pdfbox.layout.elements.render.VerticalLayout;
import rst.pdfbox.layout.elements.render.VerticalLayoutHint;
import rst.pdfbox.layout.shape.Rect;
import rst.pdfbox.layout.shape.Stroke;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Constants;
import rst.pdfbox.layout.text.Indent;
import rst.pdfbox.layout.text.SpaceUnit;

/**
 *
 * @author BUKOLA
 */
public class InvoicePDF {
    Document document = new Document(40, 60, 40, 60);
    Paragraph paragraph;
    PDPageContentStream pdStream;
    

    public void createDocument(String sender, String receiver, String invoice, String invoiceContent) throws IOException{
        setSenderDetails(sender);
        setInvoiceDetails(invoice);
        document.add(new VerticalLayout());
        document.add(new VerticalSpacer(20));
        setReceiverDetails(receiver);
        document.add(new VerticalSpacer(10));
        createHeadings();
        document.add(new VerticalSpacer(25));
//        paragraph = new Paragraph();
//        paragraph.addText("\n\n", 0f, null);
//        document.add(paragraph);
        addInvoiceItems(invoiceContent);
        OutputStream outputStream = new FileOutputStream("C:\\Users\\BUKOLA\\Desktop\\hellodoc4.pdf");
        document.save(outputStream);
        outputStream.close();
    }
    
    private void setSenderDetails(String text) throws IOException{
        paragraph = new Paragraph();
        paragraph.addText(text, 11, PDType1Font.HELVETICA);
        paragraph.setAlignment(Alignment.Left);
        paragraph.setMaxWidth(100);
        document.add(new ColumnLayout(2, 10));
        document.add(paragraph, VerticalLayoutHint.LEFT);
        
    }
    
    private void setReceiverDetails(String text) throws IOException{
        paragraph = new Paragraph();
        //document.add(new ColumnLayout(2, 10));
        paragraph.addText("BILL TO:\n", 15, PDType1Font.HELVETICA_BOLD);
        paragraph.addText(text, 11, PDType1Font.HELVETICA);
        paragraph.setAlignment(Alignment.Left);
        //paragraph.setMaxWidth(document.getPageWidth()/2);
        document.add(paragraph);//, VerticalLayoutHint.LEFT);
    }
    
    private void setInvoiceDetails(String text) throws IOException{
        paragraph = new Paragraph();
        document.add(ColumnLayout.NEWCOLUMN);
        paragraph.addText("INVOICE\n", 20, PDType1Font.HELVETICA_BOLD);
        paragraph.addText(text, 11, PDType1Font.HELVETICA);
        paragraph.setAlignment(Alignment.Right);
        //paragraph.setMaxWidth(document.getPageWidth()/2);
        document.add(paragraph);//, VerticalLayoutHint.RIGHT);
        
    }
    
    private void createHeadings() throws IOException{
        paragraph = new Paragraph();
        paragraph.add(new Indent("DESCRIPTION", 170, SpaceUnit.pt, 11, PDType1Font.HELVETICA_BOLD));
        paragraph.addText("QUANTITY     ", 11, PDType1Font.HELVETICA_BOLD);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addText("PRICE        ", 11, PDType1Font.HELVETICA_BOLD);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("       ", 11, BaseFont.Helvetica);
        paragraph.addMarkup("*TOTAL*", 11, BaseFont.Helvetica);
        //paragraph.
        Frame frame = new Frame(paragraph, document.getPageWidth(), 18f);
        frame.setShape(new Rect());
        frame.setBorder(Color.BLACK, new Stroke());
        
        frame.setPadding(5, 5, 2, 2);
        frame.setMargin(0, 0, 0, 0);
        document.add(frame, VerticalLayoutHint.CENTER);
    }
    
    private void addInvoiceItems(String invoiceItems) throws IOException{
        paragraph = new Paragraph();
        paragraph.addText(invoiceItems, 10, PDType1Font.HELVETICA);
        document.add(paragraph);
    }
    
}
