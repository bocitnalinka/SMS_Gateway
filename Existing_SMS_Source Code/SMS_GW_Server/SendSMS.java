package sms_server;

import java.io.IOException;
import java.util.Date;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

public class SendSMS {
    
    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
    
    public static void Dialog_connect(SMPPSession smpp_ses) {
        
        try {
        //    smpp_ses.connectAndBind("10.48.211.77", 2775, new BindParameter(BindType.BIND_TRX, "boctest", "test123", "", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
        //      smpp_ses.connectAndBind("192.168.160.95", 2775, new BindParameter(BindType.BIND_TRX, "boc", "boc123", "", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
             smpp_ses.connectAndBind("10.58.160.8", 2775, new BindParameter(BindType.BIND_TRX, "boc", "boc123", "", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
        } catch (IOException e) {
            System.err.println("Failed connect and bind to host at 10.58.160.8 on port 2775 in Dialog");
            System.out.println(e.toString());
        }           
    }
    
    public static void Mobitel_connect(SMPPSession smpp_ses) {
        
        try {
    //        smpp_ses.connectAndBind("202.129.232.170", 9020, new BindParameter(BindType.BIND_TX, "BOC", "boc@123", "", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));   
              smpp_ses.connectAndBind("202.129.232.173", 5016, new BindParameter(BindType.BIND_TX, "bocHO", "hO#246", "", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));         
        } catch (IOException e) {
            System.err.println("Failed connect and bind to host at 202.129.232.173 on port 5016 in Mobitel");
            System.out.println(e.toString());
        }           
    }
    
    public static void LankaBel_connect(SMPPSession smpp_ses) {
        
        try {
            smpp_ses.connectAndBind("119.235.1.79", 5019, new BindParameter(BindType.BIND_TRX, "BOC", "boc@1234", "", TypeOfNumber.UNKNOWN , NumberingPlanIndicator.UNKNOWN , null));   
        } catch (IOException e) {
            System.err.println("Failed connect and bind to host at 119.235.1.79 on port 5019 in LankaBel");
            System.out.println(e.toString());
        }           
    }     
    
    public static void disconnect(SMPPSession smpp_ses) { 
         smpp_ses.unbindAndClose();
    }
    
    public static String get_Id(SMPPSession smpp_ses) { 
         String smpp_ses_id = smpp_ses.getSessionId();
         return smpp_ses_id;
    }
       
    public static String Post_SMS (SMPPSession smpp_ses, String sending_id, String Mno, String Msg,String SenderName){
     
        String msgId="0",msgDate="";
        String date_time=timeFormatter.format(new Date());
        msgDate = "20" + date_time.substring(0,6)+"-"+date_time.substring(6,8)+":"+date_time.substring(8,10)+"."+date_time.substring(10,12); 
        String ses_Id = get_Id(smpp_ses);
        try {
         // messageId = smpp_ses.submitShortMessage("", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, sending_id, TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, Mno, new ESMClass(), (byte)0, (byte)1,  timeFormatter.format(new Date()), null, new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, new GeneralDataCoding(false, true, MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT), (byte)0, Msg.getBytes());
            msgId = smpp_ses.submitShortMessage("", TypeOfNumber.ALPHANUMERIC, NumberingPlanIndicator.UNKNOWN, sending_id, TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, Mno, new ESMClass(), (byte)0, (byte)1, "", null, new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, new GeneralDataCoding(false, true, MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT), (byte)0, Msg.getBytes());
          //  String date_time=timeFormatter.format(new Date());
          //  msgDate = "on 20" + date_time.substring(0,6)+"-"+date_time.substring(6,8)+":"+date_time.substring(8,10)+"."+date_time.substring(10,12);            
            LogFile.info("SMS |" + ses_Id + "|" +SenderName+ "|" + msgDate + "|" + Mno +" | "+ Msg +" ->|"+msgId);
          //  LogFile.info("SMS | " + msgDate + " " + Mno +" , "+ Msg +"  ->  "+msgId);
            System.out.print(msgDate);
            System.out.println(" Send id " + msgId );
        } catch (PDUException e) {
            // Invalid PDU parameter
            LogFile.info("ERR |" + ses_Id + "    |" + msgDate + "|" + Mno +" | "+ Msg +" |"+"Invalid PDU parameter");
            System.err.println("Invalid PDU parameter");
            System.out.println(e.toString());
        } catch (ResponseTimeoutException e) {
            // Response timeout
            LogFile.info("ERR |" + ses_Id + "    |" + msgDate + "|" + Mno +" | "+ Msg +" |"+"Response timeout");
            System.err.println("Response timeout");
            System.out.println(e.toString());
        } catch (InvalidResponseException e) {
            // Invalid response
            LogFile.info("ERR |" + ses_Id + "    |" + msgDate + "|" + Mno +" | "+ Msg +" |"+"Receive invalid respose");
            System.err.println("Receive invalid respose");
            System.out.println(e.toString());
        } catch (NegativeResponseException e) {
            // Receiving negative response (non-zero command_status)
            LogFile.info("ERR |" + ses_Id + "    |" + msgDate + "|" + Mno +" | "+ Msg +" |"+"Receive negative response");            
            System.err.println("Receive negative response");
            System.out.println(e.toString());
        } catch (IOException e) {
            LogFile.info("ERR |" + ses_Id + "    |" + msgDate + "|" + Mno +" | "+ Msg +" |"+"IO error occur >>> "+e.toString());            
            System.err.println("IO error occur ->>> ");
            System.out.println(e.toString());
        } catch (Exception e) {
            LogFile.info("ERR |" + ses_Id + "    |" + msgDate + "|" + Mno +" | "+ Msg +" |"+"Un identified Error");            
            System.err.println("Un identified Error");
            System.err.println("Mobile No "+Mno);
            System.err.println("Message "+Msg);            
            System.out.println(e.toString());
        }        
        return msgId;
    }	
}