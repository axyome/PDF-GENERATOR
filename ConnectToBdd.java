package printing;

// imports needed to make the software working
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
  * this part is the second one.
**/


public class ConnectToBdd 
{
    //recorder to know how much orders are remaining 
    int compteur = 0;
    
    // default constructor of the class
    ConnectToBdd()
    {
        
    }
    
    
    // method to connect to the database server to get the department where come
    // from the order
    public String connection2() throws Exception
    {  
      String temp = new String();
      String service = new String();    
      String str = "SQL REQUEST TO GET THE ORDER WITHOUT STATUS";    
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setUser("USERNAME TO CONNECT DB");
      dataSource.setPassword("PASSWORD  TO CONNECT DB");
      dataSource.setServerName("IP ADDRESS OF THE DB SERVER");
      dataSource.setDatabaseName("NAME OF THE DB");
      Connection conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(str);
      
      
      // the function rs return a table who contains the strings 
      // with the data from SERVICES
      while (rs.next())
      {
          service = rs.getString("NAME OF THE COLUMN IN THE DB");     
          compteur++;// increase counter
      }
      
      rs.close(); // close resultset
      stmt.close();// close statement
      conn.close();// close connexion
      return service; // return the name of the service in String Format

    }

    // method to connect to the DB SERVER to get the department where come
    // from the order, the reference of the order and the number of articles needed.
    
    public Vector connection() throws Exception
    {
        Vector vec = new Vector();
        Vector service = new Vector() ;
        Vector reference = new Vector();
        Vector nombre = new Vector();

        String str = "SQL REQUEST TO GET ALL THE DATA FROM THE TABLE IF THE ORDER HAS NO STATUS";
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("USERNAME TO CONNECT DB");
        dataSource.setPassword("PASSWORD TO CONNECT DB");
        dataSource.setServerName("IP OF THE SQL SERVER");
        dataSource.setDatabaseName("NAME OF THE DB");
      
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(str);
      
        // add into dedicated vectors the data needed to create pdf
        while (rs.next()) 
        {
          service.add(rs.getString("SERVICE"));
          reference.add(rs.getString("REF"));
          nombre.add(rs.getString("NOMBRE"));
        }
        // closing all elements of the connexion
        rs.close();
        stmt.close();
        conn.close();
        // add vectors into another vector
        vec .add(service);
        vec .add(reference);
        vec .add(nombre);
        
        //return the vector who contains others to the caller
        return vec;
      
    }
    
    // method who return the counter to caller
    public int getCpt ()
    {
        return compteur;
    }
    
    // method to get price of the article
        public String recupPrix( String service, String ref) throws Exception
    {
          
      String str = "SQL REQUEST WHO GETS ALL THE DATA OF THE TABLE WHO CONTAINS THE PRICES WHERE SERVICE and REFERENCE CORRESPOND TO DATA GET FROM THE WEB INTERFACE";
       
      String prix = new String(); // String who will store the price
      
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setUser("USERNAME TO CONNECT DB");
      dataSource.setPassword("PASSWORD TO CONNECT DB");
      dataSource.setServerName("IP OF THE SQL SERVER");
      dataSource.setDatabaseName("NAME OF THE DATABASE");
      
      Connection conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(str);
      
      // set the value of String prix with price of the item
      while (rs.next()) 
      {
          prix = rs.getString("PRIX");
      }
      
      // closing all elements of the connexion
      rs.close();
      stmt.close();
      conn.close();
      
      // returning price of the article to caller
      return prix;
    }
        
    // method used to set the status of the orderin the DB so this one can be considered to OK
    public String setStatut (String service) throws SQLException
    {
      String str = "SQL UPDATE REQUEST TO SET THE STATUS TO WAITING TO THE ORDER WHO HAS BEEN HANDLED";
      
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setUser("USERNAME TO CONNECT DB");
      dataSource.setPassword("PASSWORD TO CONNECT DB");
      dataSource.setServerName("IP OF THE DB SERVER");
      dataSource.setDatabaseName("NAME OF THE DB");
      
      Connection conn = dataSource.getConnection();
      PreparedStatement preparedStmt = conn.prepareStatement(str);
      // execution of the request
      preparedStmt.executeUpdate();
      
      // closing all elements of connexion
      preparedStmt.close();
      conn.close();
       
      // no need to return anything, this method is setting an update in the DB.
      // however, if needed to be sure the update has been done, the function can retun a 
      // code to tell the caller it's ok or not.
      return null;
    }
    
    // this method is called to get the articles needed
    public String getArticle() throws Exception
    { 
      String article = new String();    
      String str = "SQL REQUEST TO GET ALL DATA FROM THE TABLE WHO CONTAINS ARTICLES";
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setUser("USERNAME TO CONNECT DB");
      dataSource.setPassword("PASSWORD  TO CONNECT DB");
      dataSource.setServerName("IP OF THE DB SERVER");
      dataSource.setDatabaseName("NAME OF THE DB");
      Connection conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(str);

      while (rs.next()) 
      {
          article = rs.getString("article");
      }
      
      // return the content of the String article
      return article;
        
    }
}



    
    
    
    
    

