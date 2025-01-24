package new_crc_svr;  // Last modified on 09/04/2016

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class LogFile {   //This class was added on 01/09/2013 as create a log 

    public LogFile() {
    }

    public static void info(String text) {
        SimpleDateFormat date_Format = new SimpleDateFormat("yyyy_MM_dd");
        Calendar cal = Calendar.getInstance();
        String c_date = date_Format.format(cal.getTime());
        String log_f_name = "LOg/CRC_SMSLog_" + c_date.substring(0, 4) + "_" + c_date.substring(5, 7) + "_" + c_date.substring(8, 10) + ".txt"; 		// File logfile = new File("ATMSMSLog.txt");

        File logfile = new File(log_f_name);
        try {
            FileWriter file = new FileWriter(logfile, true); // Create file 
            BufferedWriter log = new BufferedWriter(file);
            log.write(text);
            log.write("\r\n");
            log.close(); //Close the output stream
        } catch (Exception e) {
            System.err.println("Error in Log File: " + e.getMessage());
        }
    }
}

class OneConnection {

    Socket sock;
    BufferedReader crc_in = null;

    OneConnection(Socket sock) throws Exception {
        this.sock = sock;
        crc_in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }

    void getRequest() throws Exception {

        String text = "", line = "", phone="           ";
        int len,pipe_at = 0;
        text = (crc_in.readLine()).trim();
        len = text.length();
        line = text.substring(3, len);  // Removing unwanted 3 chars come with the frount from Credit Card center
        pipe_at = line.indexOf("|");
        if (pipe_at<5) {
            System.out.println("        Err0 Pipe at < 5 "); 
            LogFile.info("  Err0 Pipe at < 5 in<=== " + line + "** End **");            
        } 
        else { 
            if (pipe_at<20)               
                phone=line.substring(0, pipe_at);
            else
                phone=line.substring(0,11);       
            try {
                long p_no = Long.parseLong(phone);      // Validating whether phone number has Alpha or not      

                int  op_val=Integer.parseInt(phone.substring(0,4));
                if (op_val<1){
                   System.out.println("Err1 Invalid Mobile number " + phone); 
                   LogFile.info("      Err1 Invalid Mobile number " + phone);
                }
                else {
                    LogFile.info("                   in<=== " + line + "** End **");
                    if (phone.length()<11) {
                       System.out.println("Err2 Invalid Length Mobi. No " + phone); 
                       LogFile.info("      Err2 Invalid Length Mobi. No " + phone);                    
                    }
                    else {
                        Send_crc_sms.Send(line);                
                    }    
                }
            } catch (Exception ex) {
                LogFile.info(" Err3  Mob. Number, pipe at "+pipe_at + " Org msg is=>" + line);
              //  LogFile.info("       Exception is " + ex);
                System.out.println("  Err3  Mob. Number, pipe at "+pipe_at + phone);
                System.out.println(" Org msg=>" + line); 
            }
        }    
    }
}

public class New_CRC_Svr {

    private static int crc_port = 3051;   //=2051;  
    private static int sms_port = 2222; // sms_port=2222;
    private static String user_id = "CR_CARD"; // want to send this user ID at the connecting of SMS session
    private static String sms_ip = "172.20.50.67";
    public static ServerSocket listener;

    public static void main(String[] args) {

        try {
            listener = new ServerSocket(crc_port);
            System.out.println("@@@  Server up and running on port  " + crc_port + " @@@");
            
            while (true) {
                System.out.print("listning...");
                Socket sock = listener.accept();
                System.out.print("Done ");
                Send_crc_sms.connect(sms_ip, sms_port);
                System.out.print("SVR-connceted ");
                OneConnection client = new OneConnection(sock);
                try{
                    client.getRequest();
                }catch(Exception exe){
                    System.out.println("Err on Exception: " + exe);
                    System.out.print("   ERR ");
                }
                System.out.print(" Disconnceting ");
                Send_crc_sms.disconnect();
                System.out.println(" Disconnceted ");
            }
        } catch (IOException ioe) {
            System.out.println("Err      IOException on socket listen: " + ioe);
        } catch (Exception ex) {
            System.out.println("Err on Exception: " + ex);
        }
    }
}