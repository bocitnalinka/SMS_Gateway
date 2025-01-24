package new_atmsms;

/**
 *
 * @author Administrator
 */
import java.io.*;

public class Log_file {
    
    private static class Public {

        public Public() {
        }  
    }
    public static void info(String cur_day, String text) { 		
            String log_f_name="Log/AllSMSLog_" + cur_day.substring(0,4) + "_" + cur_day.substring(5,7) + "_" + cur_day.substring(8,10) + ".txt"; 		// File logfile = new File("ATMSMSLog.txt");
            File logfile = new File(log_f_name);
    try{			
                    FileWriter file = new FileWriter(logfile,true); // Create file 
                    BufferedWriter log = new BufferedWriter(file);
                    log.write(text);
                    log.write("\r\n");	
                    log.close(); //Close the output stream
            }catch (Exception e){
                    System.err.println("Error in Log File: " + e.getMessage());
            }
    }      
}
