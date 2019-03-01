/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import java.text.NumberFormat;
import javafx.collections.ObservableList;

/**
 *
 * @author BUKOLA
 */
public class InvoicePDFContent{
	private String sender = "94 Wilmore Road, B20 3JJ"; //get senders details
	private StringBuilder recipient = new StringBuilder();
	private StringBuilder invoiceDetails = new StringBuilder();
	private StringBuilder content = new StringBuilder();
	private final NumberFormat currency = NumberFormat.getCurrencyInstance();
	
        public String getSender(){
            return sender;
        }
	
	public String addRecipient(String recipientName, String recipientAddress,
				String recipientCity, String recipientPostCode, String recipientEmail){
		
		recipient.append(recipientName);
		recipient.append(System.lineSeparator());
		recipient.append(recipientAddress);
		recipient.append(System.lineSeparator());
		recipient.append(recipientCity);
		recipient.append(System.lineSeparator());
		recipient.append(recipientEmail);
		
		return recipient.toString();
		
	}
	
	public String addInvoiceDetails(String invoiceNo, String invoiceDate){
		invoiceDetails.append(invoiceNo);
		invoiceDetails.append(System.lineSeparator());
		invoiceDetails.append(invoiceDate);
		
		return invoiceDetails.toString();
	}
	
	
	public String addInvoiceItems(ObservableList<RateBean> items){
		items.forEach(item -> {
            content.append(item.getDescription());
            content.append("    ").append("     ").append("     ").append("     ").append("     ")
                    .append(" ").append(" ").append(" ").append(" ").append(" ");
            content.append(item.getQuantity());
            content.append(" ").append(" ").append(" ").append(" ").append(" ")
                    .append(" ").append(" ").append(" ").append(" ").append(" ");
            //content.append(currency);
            content.append(currency.format(item.getPrice()));
            content.append(" ").append(" ").append(" ").append(" ").append(" ")
                    .append(" ").append(" ").append(" ").append(" ").append(" ");
            content.append(item.getTotal());
            content.append(System.lineSeparator());
		});
		
		return content.toString();
	}	

}

