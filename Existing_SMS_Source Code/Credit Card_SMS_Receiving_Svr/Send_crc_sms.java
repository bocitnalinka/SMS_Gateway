package new_crc_svr;    // Last modified on 22/02/2016

import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Send_crc_sms {
    
    static Socket sms_skt = null;
    static PrintWriter sms_out = null;
    static BufferedReader sms_in = null;
    
    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public static void connect(String ip,int port) {
        try {
            sms_skt = new Socket(ip, port);
            sms_out = new PrintWriter(sms_skt.getOutputStream(), true);
            sms_in = new BufferedReader(new InputStreamReader(sms_skt.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host server "+ip);
            LogFile.info("Error "+"Don't know about host server "+ip+"\n"+e.toString()+"\n");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to New SMS Server : "+ip);
            LogFile.info("Error:Couldn't get I/O for the connection to New SMS Server : "+ip+"\n"+e.toString()+"\n");
        }     
    }

    public static void disconnect() throws IOException {
        
        try {  // This wass added on 10/11/2013 for delaying the closing of socket to correct the iissue, IO Error - Socket clos. It was sucessfull. Checked after two days 12/11/2013
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(Send_crc_sms.class.getName()).log(Level.SEVERE, null, ex);
        }
        sms_out.println(".");
        sms_out.flush();
        sms_in.close();
        sms_out.close();
        sms_skt.close();     
    }
        
    public static void Send(String line){
           
        String msg,reply,status;
        Date date = new Date();
	String msgDate=dateFormat.format(date);

        try { 
            sms_out.println(line);
        //    sms_out.flush();
            reply=sms_in.readLine();
            if (reply.charAt(3)=='0') {
                status=" ERR ";
            } else {
                status=" SMS ";
            }
            System.out.println(status+"->"+reply);
            LogFile.info(status+" "+msgDate+" "+line+" "+reply);
       } catch (IOException  ex) {
             System.out.println("Error occurred : Cannot write to SMS server "+ex);
             LogFile.info("Error : Cannot write to SMS Server "+"\n"+ex.toString()+"\n");
       }                
    }    
}