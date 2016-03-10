package printing;

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
  * this part is the first one.
**/
public class Printing {
    
    Vector serv = new Vector() ;
    Vector ref = new Vector() ;
    Vector nbr = new Vector() ;
    
    public static void main(String[] args) throws Exception {
        
        // this program is needed to run all the time.
        while(true)
        {
            String service = new String();
            String article = new String();
            Vector vec = new Vector();
            ConnectToBdd con1 = new ConnectToBdd();
            service = con1.connection2();
            article = con1.getArticle();
            vec = con1.connection(); 
            Traitement tr1 = new Traitement();
            if(con1.getCpt()!=0)
            {
                 tr1.compute(service, vec, con1, article);
            }
            Thread.sleep(60000) ;// if no command in the DB, wait 1 min.
        }
    } 
}
