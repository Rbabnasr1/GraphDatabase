/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphdb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import javax.xml.soap.Node;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.jdbc.Driver;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;
/**
/**
 *
 * @author rabab
 */
public class GraphDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       String yes="No";
          Scanner in =new Scanner(System.in);
       org.neo4j.driver.v1.Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "root"));
        Session session = driver.session();
        ////////////////////////////1
        System.out.println("i.Get the names of the managers, and their managed employees");
        System.out.println("******************************-------------------------------");
        StatementResult result = session.run(
                "match(e)-[:Reported_By]->(m) return  m.FirstName As Mangers,e.FirstName As Employees ,e.LastName ,e.Title");
        System.out.println("Mangers And Employess");
        System.out.println("************************");
        
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println("Manger Name : "+record.get("Mangers").asString()+ " ||  Employee Name: "+record.get("Employees").asString()
            +" "+record.get("e.LastName").asString()+"     || "+record.get("e.Title").asString());
                   
        }
        
         System.out.println("====================================================================");
           System.out.println("Employee And Orders");
        System.out.println("************************");
        
        ////////////////////2
          System.out.println("ii.Get the Employees with counts of their sold products, you need to output Employee, Product Count");
        System.out.println("******************************-------------------------------");
       
       result = session.run("Match(O:OrderD)-[:Made_By]->(n:Employees) return n.FirstName as FirstName ,n.LastName as LastName , Count(O.ProductID) AS CountProduct");

        while (result.hasNext()) {
            Record record = result.next();
            System.out.println("Employee Name : "+record.get("FirstName").asString()+" "+record.get("LastName").asString()+ "|| product Count : "+record.get("CountProduct").asInt());
                   
            
        }////////////////////////////////////3
         System.out.println("UPdate");
        System.out.println("************************");
        System.out.println("Do You Update !!!!!!!!!!!! ");
     yes=in.next();
        if(yes.equals("Y")||yes.equals("Yes")||yes.equals("y")||yes.equals("yes")){
          System.out.println("====================================================================");
          
        
         System.out.println("====================================================================");
           result = session.run( "match(a)-[:Reported_By]->(n)-[:Reported_By]->(b) create (a)-[:Report_to]->(b)");
            System.out.println("Updated Done");
             result = session.run( "match (a)-[:Report_to]->(b) return a.FirstName,b.FirstName");
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println("Employee  : "+record.get("a.FirstName").asString()+" || Report To : "+record.get("b.FirstName"));
            
        }
        
           result = session.run( "match (a)-[r:Report_to]->(b) delete r");
        
        }
///////////////////////////////////////////////////4
         System.out.println("iv.Get the orders with their details which done to the customer with the max number of orders done");
        System.out.println("******************************--------------------------------------------------------------------------");
      
        
        String Customer="";
         System.out.println("====================================================================");
          System.out.println("Max # of order ");
        System.out.println("************************");
        
          result = session.run( "match(o:OrderD)-[:Order_By]->(c:Customer) with c,count(o.orderID)as CountOrder , collect({orderID:o.orderID,orderDate:o.OrderDate})as "
                  + "orders order by CountOrder desc return c.CustomerID,CountOrder,orders limit 1");
        while (result.hasNext()) {
            Record record = result.next();
              System.out.println(record.get("c.CustomerID"));
                System.out.println(" "+record.get("orders").toString());
           
        }
        
      /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("===========================Delete=========================================");
           result = session.run( " MATCH(p:product)<-[r:Include]-(a:OrderD)-[Made_By]->(n:Employees) where n.FirstName= 'Janet' OR n.FirstName= 'Steven' DETACH delete p");
            result = session.run( " MATCH(p:product)<-[r:Include]-(a:OrderD)-[Made_By]->(n:Employees) where n.FirstName= 'Janet' OR n.FirstName= 'Steven' return p.ProductID");
            boolean found=false;
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("p.ProductID").asString());
               found=true;
           
        }
        if(found==false){
            System.out.println("Dont find the product with steven and janet ");
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
         System.out.println("================Bounes Q ====================================================");
        
             result = session.run( " MATCH(p:product)<-[:Include]-(a:OrderD)-[Made_By]->(n:Employees) return p.ProductName as Product , a.orderID As order, n.FirstName As employee");
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println("Product  : "+record.get("Product").asString()+ " Order ID  : "+record.get("order")+" BY "+record.get("employee").asString());
     
            
        }
        
              
        
    }
    
}
