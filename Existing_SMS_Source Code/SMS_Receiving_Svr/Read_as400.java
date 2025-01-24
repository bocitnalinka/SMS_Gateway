/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package new_atmsms;
/**
 *
 * @author M.W. Jayasekara  updated on 07/01/2018
 */
import java.io.*;
import java.sql.*;
import java.net.*;

public class Read_as400 {
        private static class Public {

        public Public() {
        }
    }
    public static Connection getConnection(String cdate)  {
	
        Connection conn  = null;
	String mobileno  = null;
	String tdate     = null;
	String cust_msg  = null;
        String org_msg   = null;    
        String msgToSend ="";
        String reply="";
        
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        
//        try {
//            socket = new Socket("172.20.50.67", 2222);
//            out = new PrintWriter(socket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           
        try {
            Statement stmt;
            ResultSet rs;
            int no_sms = 0;	
            String userName = "VENDUP";
            String password = "VENDUP";
                
		
   // This for Live As400 @ H/G             
            String url = "jdbc:as400:172.20.12.100;libraries=BOCPRODDTA";
                
   // This for MIMIX @ Maharagama             
        //    String url = "jdbc:as400:172.21.12.150;libraries=BOCPRODDTA";
                
            Class.forName ("com.ibm.as400.access.AS400JDBCDriver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.print("Database connected - ");
		
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT MOBNO,TXTMSG,DATE,SENT from SMS00101Z1 WHERE SENT IN (' ','R','N') AND DATE ='"+cdate+"'");

 //added on 01 07 2015                 
            try {
                 socket = new Socket("172.20.50.67", 2222);
                // socket = new Socket("172.21.50.67", 3333); // 2222); Modified on 03/10/2017 to Send SMS from DR site
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                      
		while (rs.next()) {
                    no_sms++;
                    mobileno = rs.getString("MOBNO");
                    mobileno = "94"+mobileno.trim();
                    org_msg  = rs.getString("TXTMSG");
                    cust_msg  = org_msg.trim();
             //       cust_msg  = org_msg.trim()+" - Thank you for banking with BOC."; 
             //      encoded  = URLEncorder.encode(custmsg, "UTF-8");                    
                    tdate    = rs.getString("DATE");                    

                    System.out.print("Mobile No " + mobileno +" "+ cust_msg + " " + tdate);  // +":"+ttime);
       // 28/02/2018 Move to line 98,99            
//                    rs.updateString("SENT","S");
//                    rs.updateRow();
                   
                    System.out.print("Msg to: " + mobileno+" "+cust_msg +" -> ");
                     
                    msgToSend = mobileno+cust_msg;
                    try {
                        out.println(msgToSend);
                        if (no_sms==1) {
                           reply=in.readLine();
                        }else {
                           reply=Integer.toString(no_sms);
                           if (no_sms>19){
                               no_sms=0;                               ;
                           } 
                        }   
                        System.out.println(reply);
                        out.flush();
                        
                        rs.updateString("SENT","S");
                        rs.updateRow();
                        Log_file.info(cdate,"SMS : " + tdate + " " + mobileno + " " + cust_msg + " " + reply);                        
                    } catch (IOException  ex) {
                        System.out.println("Error occurred at delivering to SMS GateWay "+ex);
                        Log_file.info(cdate,"ERR : " + tdate + " " + mobileno + " " + cust_msg + " Error occurred at delivering to SMS GateWay "+ex);                      
                    }
            //  28/02/2018        
           //         Log_file.info(cdate,"SMS : " + tdate + " " + mobileno + " " + cust_msg + " " + reply);
		}
//added on 11072015              
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: server");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: New SMS Server");
                System.exit(1);
            }
//added on 11072015
                
	} catch (Exception e) {
            System.err.println("Error in Connect with ATMinfo: " + e.getMessage());
	}
                      
	finally {
            if (conn != null) {
                try {
		   conn.close ();
		   System.out.println ("Done! & terminated");
		}
			   catch (Exception e) { /* ignore close errors */ }
	   }
	}
	return null; // conn;   
    }            
}