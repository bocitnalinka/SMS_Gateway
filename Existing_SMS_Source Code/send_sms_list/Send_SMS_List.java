
package send_sms_list;

import java.net.*;
import java.io.*;

public class Send_SMS_List {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        BufferedReader f1; // = null;
        BufferedReader f2; // = null;
        String mobileno; // = null;
        String msgToSend; // = null;
        String msg,reply;
        
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket("172.20.50.67", 3312);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: server 172.20.50.67 ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: 172.20.50.67 via port 2222");
            System.exit(1);
        }
        try {
                f1=new BufferedReader(new FileReader("mobno.txt"));
                f2=new BufferedReader(new FileReader("msg.txt"));   //  " "C:\\creditcard\\sms\\msg.txt"));
                msgToSend = f2.readLine().trim();
                while ( (mobileno =f1.readLine().trim()) != null) {
  
                    System.out.print("Msg to:"+mobileno+" -> ");
                    if (mobileno.substring(0,2).equals("94" )) {
                        msg=mobileno+msgToSend;                        
                    } else {
                        msg=mobileno+"|"+msgToSend;                                                
                    }
                    out.println(msg);
                    reply=in.readLine();
                    System.out.println(reply);
                    out.flush();
                    Thread.sleep(150); // This is for delaying
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