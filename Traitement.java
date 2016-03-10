package printing;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;



/**
 *
 * @author s.theismann
 */

 /**
  * the entire program is paired with a sql server to stores data and a web interface
  * who allows partners to make commands in toners for their printers.
  * the command must be generated into a document with a strict shaping.
  * the goal of this software is to create the document into a pdf format and print it on
  * a dedicated printer.
  * 
  * the software is break into 3 parts who communicate : 
  * One is the master and organize the actions of the different parts (printing), 
  * he contains the main method. 
  * Second is to connect to database and get the required data to fulfill the form
  * and the third is to receipt the data, handle them, make the document and print it.
  *
  * this part is the third one.
**/
public class Traitement {
    
        // default constructor of the class "traitement"
        Traitement()
        {
            
        }
    
        // method who uses the itextpdf library to create the pdf with the data
        // from the DB
        public void compute(String service, Vector vec,ConnectToBdd cn1, String article) throws IOException, DocumentException, InterruptedException, Exception
        {     
            
            
            // variables contenant les articles commandés
            String [] cmd = {"", "", "", "", "", "", "", "", "", ""};
            String total = new String("");
            
            // valeur numérique du total
            float val = 0;
            
            
            //création des vecteurs de traitement des infos
            Vector serv = new Vector() ;
            Vector ref = new Vector() ;
            Vector nbr = new Vector() ;
            
            
            // récupération des infos dans vec pour les dispatcher dans les 3 vecteurs de traitement
            
            serv = (Vector)vec.get(0);
            ref = (Vector)vec.get(1);
            nbr = (Vector)vec.get(2);
            
            // traitement des données des vecteurs
                    cmd[cn1.getCpt()-1]=ref.get(cn1.getCpt()-1).toString(); 
                    String string = cmd[cn1.getCpt()-1];
                    String[] parts = string.split("\n");       
            
            // calcul des valeurs des produits
                    
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    
                    
                  
        for (int i=0; i < parts.length; i++ )// ajout des consommables demandés au bon de commande
        {
              val = val + Float.parseFloat(cn1.recupPrix(service, parts[i]).replace(",", "."));
              df.format(val); //formattage de val a 2 chiffres apres la virgule
        }
                         
            // ajout de la valeur du produit total   
        
            // calcul de la TVA
            val = val+ ((val/100)*21);
            total = total+df.format(val);

        // creer pdf de sortie
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("PATH TO THE DIRECTORY TO STORES PDF"));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        
        //charger pdf d'entrée
        PdfReader reader = new PdfReader(new FileInputStream("PATH TO THE DIRECTORY TO STORES PDF"));
        PdfImportedPage page = writer.getImportedPage(reader, 1);
        
        // copie du template vers le pdf de sortie
        document.newPage();
        // ajout du début du document avec le logo et les coordonnées du fournisseur
        cb.addTemplate(page,0,0);
        
        Font f=new Font(FontFamily.HELVETICA,10.0f,Font.NORMAL,BaseColor.BLACK);
        Paragraph p = new Paragraph();
        p.setFont(f);
        // chuncks pour les parties soulignées a générée
        Chunk pUnderline = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline2 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline2bis = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline3 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline4 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline5 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline6 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline7 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline8 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline9 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline10 = new Chunk("TEXT TO ADD TO THE PDF");
        Chunk pUnderline11 = new Chunk("TEXT TO ADD TO THE PDF");
        
        
        
        //underlining des chunks
        pUnderline.setUnderline(1, -1);
        pUnderline2.setUnderline(1, -1);
        pUnderline2bis.setUnderline(1, -1);
        pUnderline3.setUnderline(1, -1);
        pUnderline4.setUnderline(1, -1);
        pUnderline5.setUnderline(1, -1);
        pUnderline6.setUnderline(1, -2);
        pUnderline7.setUnderline(1, -2);
        pUnderline8.setUnderline(1, -2);
        pUnderline9.setUnderline(1, -2);
        pUnderline10.setUnderline(1, -2);
        pUnderline11.setUnderline(1, -2);
        
        // récupération de la date de génération du bon
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = localCalendar.getTime();
        String dat2 = new String(String.format("%1$td/%1$tm/%1$ty",currentTime));
        
        // ajout des éléments au document pour génération
        p.setIndentationLeft(35);
        p.add("\r\r\r\r\r\r\r\r\r\r\r\r\r");
        p.add("\r"+service+"\r");
        p.add(pUnderline);
        p.add(" : "+article+"\r");
        p.add(pUnderline2);
        p.add(" : \r");
        
        for (int i=0; i < parts.length; i++ )// ajout des consommables demandés au bon de commande
        {
            p.add("\r 1 X "+parts[i]+"      (Prix HTVA :    "+cn1.recupPrix(service, parts[i])+")");
        }
        
        p.add("TEXT TO ADD TO THE PDF"); 
        p.add(pUnderline2bis);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(total);
        p.add("TEXT TO ADD TO THE PDF");
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline3);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline4);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline5);
        p.add("TEXT TO ADD TO THE PDF"+dat2+"TEXT TO ADD TO THE PDF");
        p.add(pUnderline6);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline7);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline8);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline9);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline10);
        p.add("TEXT TO ADD TO THE PDF");
        p.add(pUnderline11);
        p.add("TEXT TO ADD TO THE PDF");
        
        p.add("TEXT TO ADD TO THE PDF");
        p.add("TEXT TO ADD TO THE PDF");
        p.add("TEXT TO ADD TO THE PDF");
        p.add("TEXT TO ADD TO THE PDF");
        p.add("TEXT TO ADD TO THE PDF");
        
        
        
        // ajout de tout le canvas au document PDF de sortie
        document.add(p);
        // fermeture du document
        document.close();
        
        print(service);
                    
        cn1.setStatut(service);
        
        }
        
        // imprimer le pdf généré
        public void print(String service) throws IOException, InterruptedException
        {
            // récupération de la date du jour
            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
            Date currentTime = localCalendar.getTime();
            String dat = new String(String.format("%1$td-%1$tm-%1$ty---%1$tH-%1$tM",currentTime));
            
            //impression du fichier sur l'imprimante par défaut
            File file = new File("PATH TO THE FOLDER WHO STORES ORDERS");
            Desktop.getDesktop().print(file);
            
            
            // déplacement du fichier + renaming
            Thread.sleep(7000) ;
            File destination = new File("PATH TO THE FOLDER WHO STORES ORDERS WITH DATE (DD/MM hh/mm so no confusion in naming)");
            file.renameTo(destination);
            
            
        }
    
}
