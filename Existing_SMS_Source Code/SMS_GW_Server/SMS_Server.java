/*
 * To change this template, choose Tools | Templates  and open the template in the editor.
 * 
 * SMS Sending Application which connects with mobile operators via SMPP
 * Programme Ver 1.31 Moddified on 01.10.2013, added Mobitel Connectivity
 * 
 * Programme Ver 1.41 Moddified on 19.10.2013, Modified the SMPP Session connecting method 
 * 
 * Programme Ver 1.51 Moddified on 13.07.2015, Modified the SMPP Session connecting method 
 * 
 * Programme Ver 1.52 Moddified on 02.08.2015, Modified the SMPP Session connecting method 
 * 
 * Programme Ver 2.00 Modify to send International SMS's via Lankabel
 * 
 * Completed on 04/04/2016 Pipe line seprator added to the server in ver 2.0
 * 
 * Modify post pipeline implementation issues on 09/04/2016 Ver 2.10
 * 
 * Corrected all issues related to the Ver 2.10modiication on 22/04/2016 Ver 2.20
 *
 * Change the log file format on 25.03.2018 Ver 2.5
 * 
 * Change  Divert Mobitel traffic to Lnaka Bel  on 29032018  Ver 2.51
 *
 * Revert Back Mobitel traffic to New Mobitel VPN 17042018  Ver 2.52
 * 
 * Add SMS diverte operator's Code to the Log 17/04/2018
 */
package sms_server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.jsmpp.extra.SessionState;

import org.jsmpp.session.SMPPSession;

class LogFile {   //This class was added on 01/09/2013 as create a log 

    public LogFile() {}
    public static void info(String text) {
        
	// File logfile = new File("Dia_Mob_SMSLog.txt");

        SimpleDateFormat date_Format = new SimpleDateFormat("yyyy_MM_dd");
  	Calendar cal = Calendar.getInstance();
        String c_date = date_Format.format(cal.getTime());
        String log_f_name="LOg/SMSLog_" + c_date.substring(0,4) + "_" + c_date.substring(5,7) + "_" + c_date.substring(8,10) + ".txt"; 		// File logfile = new File("ATMSMSLog.txt");
        
        File logfile = new File(log_f_name);
        try{			
            FileWriter file = new FileWriter(logfile,true); // Create Log file if not exist 
            BufferedWriter log = new BufferedWriter(file);
            log.write(text);
            log.write("\r\n");	
            log.close(); //Close the output stream
        }catch (Exception e){                               
		System.err.println("Error in Log File: " + e.getMessage());
	}
    }
}

class doComms implements Runnable {
    private Socket server;
    private SMPPSession smpp_Dia;
    private SMPPSession smpp_Mob;        
    private SMPPSession smpp_LB;
    private String Sender_Name;
    private String line,msgid="0";
    private int pos,len;

//  doComms(Socket server, SMPPSession smpp_Dia, SMPPSession smpp_Mob) {    
    doComms(Socket server, SMPPSession smpp_Dia, SMPPSession smpp_Mob, SMPPSession smpp_LB) {
      this.server=server;
      this.smpp_Dia=smpp_Dia;
      this.smpp_Mob=smpp_Mob;
      this.smpp_LB=smpp_LB;
    }

    @Override
    public void run () {
        
       String Dilog_id="BOC"; //test_id="89891", Live_id="8825"
       String Mobitel_id="BOC";
       String LBel_id="94115923261";
       String Mo_op, Mop="LB  ", head, phone = " ", msg=" ", ses_Id="0000000", Exit="F";
       
   //  int pos, len;  change  on 25/3/2018 move to line 67     
       
       pos=0;
       len=0;
       
       try {
          DataInputStream in = new DataInputStream (server.getInputStream());
          PrintStream out = new PrintStream(server.getOutputStream());

          while((line = in.readLine()) != null && !line.equals(".")) {
             line=line.trim();
             // System.out.println(line.substring(0,15));
             Mo_op=line.substring(2,3);               // To Check only the first digit of Operator Code
             if ((line.length()>=11) ) {      // Line length less than 11 chars
                 if (line.length() < 182) {  // Line contains Mobileno+msg                      
                      if (line.substring(0,2).equals("94")) { //Local number and Mobile no lenth is 11
                          Mo_op=line.substring(2,4);               // To get Correct Operator Code
                          Mop="LB  ";
                          Sender_Name="LnB";
                          if (Mo_op.equals("77") || Mo_op.equals("76")) {
                              Mop="Dia ";
                              Sender_Name="Dia";
                          } else {
                              if (Mo_op.equals("71")|| Mo_op.equals("70")) {
                                  Mop="Mob ";
                                  Sender_Name="Mob";
                              }
                          }  
                          phone=line.substring(0,11); 
                          if (line.substring(11,12).equals("|")) {
                              msg=line.substring(12,line.length());                                 
                          } else {
                              msg=line.substring(11,line.length());
                          }                                  
                      } else {
                           head = line.substring(0,18);
                           pos = 6;
                           len=head.length();
                           try {
                                while (!((head.charAt(pos)=='|') || (head.charAt(pos)=='R')) && (pos < len)) {
                                     pos++;               
                                }
                                //pos = head.indexOf("|");
                                System.out.println("Current POS is => "+pos);
                                if (pos>15) { // There is no separater like '|'                                        
                                   phone=line.substring(0,11); 
                                   msg=line.substring(11,line.length());
                                 //  LogFile.info("Line 119 No pipe up to pos " + pos);
                                 //  LogFile.info("         No pipe to separate-->"+phone+"<-->"+msg);
                                   Exit="F";
                                } else {
                                    if (pos<10) {
                                        Exit="T";
                                    }
                                    else {
                                        len=line.length();
                                        phone=line.substring(0,pos);
                                        msg=line.substring(pos+1,len);
                                    //    LogFile.info("Line 126 pipe may be at " + pos);
                                    //    LogFile.info("         Pipe separated at " +pos+"-->"+phone+"<-->"+msg);
                                        Exit="F";
                                    }    
                                }
                           } catch (Exception ex) {
                               LogFile.info("Err |            |                 |" + line + "|Exception at serch separator in Org msg ");
                               System.out.println("Exception at serch | separator ");
                               LogFile.info("Msg | |"+Sender_Name+"| | | |Above Exception is " + ex);
                               System.out.println("Exception " + ex);
                               Exit="T";
                           }
                      }
                      if (Exit.equals("T")) {
                          return;
                      }
                      else {
                         if (phone.length()>=8) {
                            try { 
                              long p_no = Long.parseLong(phone);
                              int  op_val=Integer.parseInt(phone.substring(0,4));
                              System.out.print(phone+" => ");  // + msg); 
                              switch (op_val) {

                                  case 9477:  //Need to be Corrected as 9477
                                       ses_Id = SendSMS.get_Id(smpp_Dia)+"|Dia";
                                       System.out.print(ses_Id+ " ");
                                       msgid = SendSMS.Post_SMS(smpp_Dia, Dilog_id, phone, msg,Sender_Name);

                                       if (msgid.equals("0")) {
                                          Thread.sleep(100); 
                                          System.out.println("==================> Resending the message after sleep 1 second via Dialog ==========>"); 

                                         smpp_Dia = new SMPPSession();
                                         SendSMS.Dialog_connect(smpp_Dia);
                                         ses_Id = SendSMS.get_Id(smpp_Dia)+"|Dia";
                                         msgid = SendSMS.Post_SMS(smpp_Dia, Dilog_id, phone, msg,Sender_Name);
                                       }
                                       break;

                                  case 9476: 
                                       ses_Id = SendSMS.get_Id(smpp_Dia)+"|Dia";
                                       System.out.print(ses_Id+ " ");
                                       msgid = SendSMS.Post_SMS(smpp_Dia, Dilog_id, phone, msg,Sender_Name);

                // ********** Re Started Message resending from 12/08/2014                         
                                       if (msgid.equals("0")) {
                                          Thread.sleep(100); 
                                          System.out.println("==================> Resending the message after sleep 1 second via Dialog==========>"); 

                                          smpp_Dia = new SMPPSession();
                                          SendSMS.Dialog_connect(smpp_Dia);
                                          ses_Id = SendSMS.get_Id(smpp_Dia)+"|Dia";
                                          msgid = SendSMS.Post_SMS(smpp_Dia, Dilog_id, phone, msg,Sender_Name);
                                       }
                                       break;
                                  case 9471:
                                       ses_Id = SendSMS.get_Id(smpp_Mob)+"|Mob";
                                       System.out.print(ses_Id+ " ");
                                       msgid = SendSMS.Post_SMS(smpp_Mob, Mobitel_id, phone, msg,Sender_Name);

                                      if (msgid.equals("0")) {
                                //          Thread.sleep(100);   //Sinc we used Lanka Bel we need not sleep 
                                          System.out.println("==================> Resending the message via LankaBel without sleep  ==========>"); 
                                          ses_Id = SendSMS.get_Id(smpp_LB)+"|Mob";
                                          msgid = SendSMS.Post_SMS(smpp_LB, LBel_id, phone, msg,Sender_Name);
                                      } 
                                       break;
                                  case 9470:
                                       ses_Id = SendSMS.get_Id(smpp_Mob)+"|Mob";
                                       System.out.print(ses_Id+ " ");
                                       msgid = SendSMS.Post_SMS(smpp_Mob, Mobitel_id, phone, msg,Sender_Name);

                                      if (msgid.equals("0")) {
                                //          Thread.sleep(100);   //Sinc we used Lanka Bel we need not sleep 
                                          System.out.println("==================> Resending the message via LankaBel without sleep  ==========>"); 
                                          ses_Id = SendSMS.get_Id(smpp_LB)+"|Mob";
                                          msgid = SendSMS.Post_SMS(smpp_LB, LBel_id, phone, msg,Sender_Name);
                                      } 
                                       break;
                                      
                                  default:
                                       Mop="LB  ";
                                       ses_Id = SendSMS.get_Id(smpp_LB)+"|LnB";
                                       System.out.print(ses_Id+ " ");
                                       msgid = SendSMS.Post_SMS(smpp_LB, LBel_id, phone, msg,Sender_Name);

                                       if (msgid.equals("0")) {
                               //         Thread.sleep(100);  //Sinc we used Mobitel we need not sleep
                                          System.out.println("==================> Resending the message via Mobitel without sleep  ==========>"); 
             // *** Updated on 29032018        Divert Mobitel traffic to Lnaka Bel                                
                    /*                      ses_Id = SendSMS.get_Id(smpp_Mob)+"|"+Sender_Name;
                                          msgid = SendSMS.Post_SMS(smpp_Mob, Mobitel_id, phone, msg,Sender_Name);
                    */ 
                                           ses_Id = SendSMS.get_Id(smpp_Dia)+"|Dia";
                                          msgid = SendSMS.Post_SMS(smpp_Dia, Dilog_id, phone, msg,Sender_Name);
                                       }
                                      break;
                              }
                              if (Mop.equals("LB  ")) {
                                 out.println("Id=11011,"+ "Mob=" + phone); 
                              } else {
                                 out.println("Id="+msgid +","+ "Mob=" + phone); // +","+ "Msg="+msg;
                              }      
                         }catch(Exception ex) {
                                 long p_no = 9900;
                                 LogFile.info("Err |        |"+Sender_Name+"|                 |" +line.substring(0,pos)+" | "+line.substring(pos+1,line.length())+"->| Invalid phone number"); 
                          //     LogFile.info("Err |            |                 |" +line+"| Invalid phone number"); 
                                 System.out.println("Error Invalid Phone number 1:  "+phone + ex);
                                 out.println("Id=0,Invalid Phone number,"+ "Mob=" + phone);
                         }
                     } else {
                           LogFile.info("Err |        |"+Sender_Name+"|                 |" + line+"->|Phone nunber less than 8 chars");
                           System.out.println("Err       Msg Unable to Send via SMS GW to the Invalid Mobile No " + phone);               
                     }                          
                }  // Not Exit (This will be true if prg get exception while separating phone and msg
            }
            else{
                if (line.length()>11) {  // If comes here & That means Operator code is not start with 7  
                    LogFile.info("Err |        |"+Sender_Name+"|                 |" +line+"->|Invalid Mobile Operator Code");
                    out.println("Id=0,Invalid Mobile Operator - Operator Code = 00 ");
                    System.out.println("Invalid Mobile Operator Code 00 ");
                }
                else {
                    LogFile.info("Err |        |"+Sender_Name+"|                 |" +line+"->|Invalid Message length < 11 chars ");
                    out.println("Id=0,Invalid Message & Mobile Number - Msg length < 11 chars");
                    System.out.println("Invalid Message or Mobile Number < 11 Char");
                }    
            }
        } else {
            // LogFile.info("Err | Invalid Message Line length < 11 chars " +line.substring(0,11)+" |"+line.substring(11,line.length()));
            LogFile.info("Err |       |"+Sender_Name+"|                 |" +line+"->|Invalid Message Line length < 11 chars");    
            out.println("Id=0,Total Msg length < 11 chars");
            System.out.println("Invalid Message Total Msg length < 11 Char");       
        }
        
      } // End of while loop 
        
      } catch (IOException ioe) {
        System.out.println("IOException on socket listen 06042016: " + ioe);
      }
   }
}

public class SMS_Server {

  private static int port=2222; // old port 2222, used 3333 for testing , maxConnections=10;

  public static void main(String[] args) {
        
    try{
        ServerSocket listener = new ServerSocket(port);
        LogFile.info("Msg | | | | |Server Ver 2.520(18/04/2018) up and running on port " + port +" ->| ");
        System.out.println("Server Ver 2.52 (18/04/2018) up and running on port " + port );

        Socket server;
        
        SMPPSession smpp_Dia;
        SMPPSession smpp_Mob;
        SMPPSession smpp_LB;
        
        smpp_Dia = new SMPPSession();
        smpp_Mob = new SMPPSession();
        smpp_LB  = new SMPPSession();

        SendSMS.Dialog_connect(smpp_Dia);
        SendSMS.Mobitel_connect(smpp_Mob);
        SendSMS.LankaBel_connect(smpp_LB);

        while (true){ //((i++ < maxConnections) || (maxConnections == 0)){    

            if (!smpp_Dia.getSessionState().isBound() || smpp_Dia.getSessionState().equals(SessionState.CLOSED) ) {
             smpp_Dia = new SMPPSession(); 
             SendSMS.Dialog_connect(smpp_Dia);
            }   
            
            if (!smpp_Mob.getSessionState().isBound() || smpp_Mob.getSessionState().equals(SessionState.CLOSED) ) {
             smpp_Mob = new SMPPSession();
             SendSMS.Mobitel_connect(smpp_Mob);
            } 
                
            if (!smpp_LB.getSessionState().isBound() || smpp_LB.getSessionState().equals(SessionState.CLOSED) ) {
             smpp_LB = new SMPPSession();
             SendSMS.LankaBel_connect(smpp_LB);
            }
            server = listener.accept();
            doComms conn_c= new doComms(server, smpp_Dia, smpp_Mob, smpp_LB);
            Thread t = new Thread(conn_c);
            t.start();                                     
         //   String Dia_ses_Id = SendSMS.get_Id(smpp_Dia);
         //   String Mob_ses_Id = SendSMS.get_Id(smpp_Mob);
        }
    } catch (IOException ioe) {
        System.out.println("IOException on socket listen: " + ioe);
    }
  }
}