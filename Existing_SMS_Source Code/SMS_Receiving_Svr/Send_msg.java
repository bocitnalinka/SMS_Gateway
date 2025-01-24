/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package new_atmsms;

/**
 * @author Administrator
 */
import java.io.*;
import java.net.*;

public class Send_msg {
    
    private static class Public {

        public Public() {
        }  
    }
    public static String SMS(String Mobino, String Messag) {
        String requestUrl = "http://172.20.50.67/SMS?Mno="+Mobino+"&Msg="+Messag;
        String result = null;
        try {
            URL u = new URL(requestUrl.toString());	
            BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream())); 
            result = in.readLine();
            in.close();
        } catch (IOException e) {
            System.err.println("Error in SendSMS: " + e.getMessage());
            Log_file.info("99/99/9999","ERR : Error in SendSMS: " + e.getMessage());
        }
        return result;
    }
}
