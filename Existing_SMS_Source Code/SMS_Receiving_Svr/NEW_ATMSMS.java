/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package new_atmsms;

/**
 *
 * @author Administrator
 */
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NEW_ATMSMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

     //   Connection cn = null;
        String cdate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
        while (true) {
            Calendar cal = Calendar.getInstance();
		
            try {
                cdate = dateFormat.format(cal.getTime());
         //       cn = Read_as400.getConnection(cdate);
                Read_as400.getConnection(cdate);
                System.out.print("D:N_AllSMS_Live> " + cdate + " Waiting... ");
                Thread.sleep(10000); 
            }catch (Exception e){
                System.out.println("Error Thread creation in Main");
            }
        }
    }
}
