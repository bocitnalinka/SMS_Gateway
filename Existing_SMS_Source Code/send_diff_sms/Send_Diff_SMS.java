/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package send_diff_sms;

import java.net.*;
import java.io.*;

public class Send_Diff_SMS {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        BufferedReader f1; // = null;
        BufferedReader f2; // = null;
        String mobileno; // = null;
        String msgToSend; // = null;
        String msg,reply;
        int no_sms =0;
        
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket("172.20.50.67", 2222);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: server");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: New SMS Server");
            System.exit(1);
        }
        try {
                f1=new BufferedReader(new FileReader("mobnomsg.txt"));
    //            f2=new BufferedReader(new FileReader("msg.txt"));   //  " "C:\\creditcard\\sms\\msg.txt"));
    //            msgToSend = f1.readLine();
                while ( (msg = f1.readLine()) != null) {
                    no_sms++;
                    msg = msg.trim();
                    if ("0".equals(msg.substring(0,1))) {
                        msgToSend = "94"+msg.substring(1,msg.length());
                    } else {
                        msgToSend = msg;
                    }    
                    System.out.print("Msg to: "+msgToSend+" -> ");
                    
                    out.println(msgToSend);
                    if (no_sms==1) {
                       reply=in.readLine();
                    }else {
                       reply=Integer.toString(no_sms);
                       if (no_sms>9){
                           no_sms=0;
                               Thread.sleep(2000); // This is for delaying
                       } 
                    } 
                    System.out.println(reply);
                    out.flush();
         //           Thread.sleep(150); // This is for delaying
                }   
                
                out.println(".");

       } catch (IOException  ex) {
             System.out.println("Error occurred:::"+ex);
       }        
        in.close();
        out.close();
        socket.close();
    }
}

