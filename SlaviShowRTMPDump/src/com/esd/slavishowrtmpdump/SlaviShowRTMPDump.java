package com.esd.slavishowrtmpdump;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.text.BadLocationException;

import java.awt.Font;

import javax.swing.ImageIcon;

public class SlaviShowRTMPDump extends JFrame implements ActionListener, FlavorListener
{
   /**
    * 
    */
   private static final long   serialVersionUID = -5521721082291398815L;

   private final static String MP4_BGN = "\"url\":\"slavishow/",
                               MP4_END = "\",",
//                               URL_ENC_BGN = "<div class=\"fb-like\" data-href=\"",
//                               URL_ENC_END = "\"",
                               NAME_BGN = "www.slavishow.com/",
                               NAME_END = "/",
                               RTMP_DUMP_CMD = "rtmpdump.exe -v -r \"rtmp://video.slavishow.com/slavishow/\" -a \"slavishow/\" -f \"WIN 13,0,0,214\" -W \"http://www.slavishow.com/content/themes/slavishow/swf/flowplayer.commercial-3.2.18.swf\" -p \"%s\" -y \"slavishow/%s\" --tcUrl \"rtmp://video.slavishow.com/slavishow/\" -R --buffer 2000 -o \"%s%s.flv\"",
//                               RTMP_DUMP_PATH = "D:\\Iasen\\Documents\\Tools\\rtmpdump-2.4-git-010913-windows\\",
                               RTMP_DUMP_PATH = "",
//                               DWN_PATH = "D:\\",
                               PRG_PATTERN = "\\d{1,6}\\.\\d{3} kB / \\d{1,3}\\.\\d{2} sec \\(\\d{1,2}\\.\\d{1}%\\)",
                               DOMAIN = "slavishow.com";
   

   private final static String lat              = "A B V G D E J Z I Y K L M N O P R S T U F H C Ch Sh Sht Y Yu Ya a b v g d e j z i y k l m n o p r s t u f h c ch sh sht y y yu ya";

   // private final static String cyr =
   // "A Б В Г Д Е Ж З И Й К Л М Н О П Р С Т У Ф Х Ц Ч Ш Щ Ъ Ю Я а б в г д е ж з и й к л м н о п р с т у ф х ц ч ш щ ъ ь ю я";
   private final static String cyr              = "\u0410 \u0411 \u0412 \u0413 \u0414 \u0415 \u0416 \u0417 \u0418 \u0419 \u041A \u041B \u041C \u041D \u041E \u041F \u0420 \u0421 \u0422 \u0423 \u0424 \u0425 \u0426 \u0427 \u0428 \u0429 \u042A \u042E \u042F \u0430 \u0431 \u0432 \u0433 \u0434 \u0435 \u0436 \u0437 \u0438 \u0439 \u043A \u043B \u043C \u043D \u043E \u043F \u0440 \u0441 \u0442 \u0443 \u0444 \u0445 \u0446 \u0447 \u0448 \u0449 \u044A \u044C \u044E \u044F";

   private boolean                     bIsStarted = false;
   
   private static String               sVersion;
   
   private String                      sRTMPDump;

   private JButton                     btnGet;

   private JTextArea                   taCommand;
   
   private JTextField                  txtURL;

   private Pattern                     oPattern;

   private Matcher                     oMatcher;
   
   private Clipboard                   oClipboard;
   
   private RTMPDumpThread              oRTMPDumpThread;

   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            try
            {
               Package p = this.getClass().getPackage();
               sVersion = p.getImplementationVersion();
               
               new SlaviShowRTMPDump();
               
            } 
            catch(Exception e)
            {
               e.printStackTrace();
            }
         }
      });
   }


   public SlaviShowRTMPDump()
   {
      super("Slavi Show RTMPDump Tool " + sVersion);
      
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      Container container = this.getContentPane();

      getContentPane().setLayout(new BorderLayout(0, 0));

      JPanel panel = new JPanel();
      getContentPane().add(panel, BorderLayout.NORTH);
      panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

      txtURL = new JTextField();
      txtURL.setFont(new Font("Courier New", Font.PLAIN, 11));
//      txtURL.setText("http://www.slavishow.com/\u043a\u0440\u0438\u0441\u0438\u044f-\u0442\u043e\u0434\u043e\u0440\u043e\u0432\u0430-simply-the-best/");
      panel.add(txtURL);
      txtURL.setColumns(10);

      btnGet = new JButton("");
      btnGet.setIcon(new ImageIcon(SlaviShowRTMPDump.class.getResource("/icons/play.png")));
      btnGet.addActionListener(this);
      
      panel.add(btnGet);

      taCommand = new JTextArea(0, 20);
      taCommand.setFont(new Font("Courier New", Font.PLAIN, 13));
      taCommand.setLineWrap(true);

      JScrollPane scrollPane = new JScrollPane(taCommand,
                                               ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                               ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      getContentPane().add(scrollPane);

      this.setSize(350, 300);
      this.setVisible(true);

      oClipboard = getSystemClipboard();
      flavorsChanged(null);
      
      Runtime.getRuntime().addShutdownHook(new Thread()
      {
          @Override
          public void run()
          {
//             System.out.println("ShutDown");
             
//             if(oRTMPDumpThread != null && oRTMPDumpThread.isAlive())
             if(bIsStarted)
             {
                vStop();
//                oRTMPDumpThread.interrupt();
             }
          }
      });      

   }

   @Override
   public void actionPerformed(ActionEvent ae)
   {
      Object oSource = ae.getSource();

      if(oSource == btnGet)
      {
         if(!bIsStarted)
         {
            String sURL = txtURL.getText();
            if(!sURL.contains(DOMAIN))
               return;
            
            bIsStarted = true;
            
            btnGet.setIcon(new ImageIcon(SlaviShowRTMPDump.class.getResource("/icons/stop.png")));
            
            

            int iNameBgn = sURL.indexOf(NAME_BGN);
            int iNameEnd = sURL.indexOf(NAME_END, iNameBgn + NAME_BGN.length());
            String sName = sURL.substring(iNameBgn + NAME_BGN.length(), iNameEnd);

            try
            {
               String sNameEnc = URLEncoder.encode(sName, "UTF-8");

               String sURLEnc = "http://www.slavishow.com/" + sNameEnc + "/";

               String sResponse = getFromURL(sURLEnc);

               int iMP4Bgn = sResponse.indexOf(MP4_BGN);
               int iMP4End = sResponse.indexOf(MP4_END, iMP4Bgn);
               String sMP4 = sResponse.substring(iMP4Bgn + MP4_BGN.length(), iMP4End);

               // int iURLEncBgn = sResponse.indexOf(URL_ENC_BGN);
               // int iURLEncEnd = sResponse.indexOf(URL_ENC_END, iURLEncBgn);
               // String sURLEnc = sResponse.substring(iURLEncBgn +
               // URL_ENC_BGN.length(), iURLEncEnd);

               // int iNameBgn = sURL.indexOf(NAME_BGN);
               // int iNameEnd = sURL.indexOf(NAME_END, iNameBgn);
               // String sName = sURL.substring(iNameBgn + NAME_BGN.length(),
               // iNameEnd);

               int iDateEnd = sMP4.indexOf("_");

               String sDate = sMP4.substring(0, iDateEnd);

               sName = sDate + "_" + sName;

               String sNameLat = cyr2lat(sName);

               String sHome = System.getProperty("user.home");
               
               String sDwnPath; 
               
               if((new File(sHome + "\\Downloads\\")).exists())
                  sDwnPath = sHome + "\\Downloads\\";
               else if((new File(sHome + "\\Download\\")).exists())
                  sDwnPath = sHome + "\\Download\\";
               else
               {
                  (new File(sHome + "\\Downloads\\")).createNewFile();
                  sDwnPath = sHome + "\\Downloads\\";               
               }

               final String sRTMPDumpCmd = String.format(RTMP_DUMP_CMD, sURLEnc, sMP4, sDwnPath, sNameLat);
               
               taCommand.setText(sRTMPDumpCmd);

               // command = Runtime.getRuntime().exec("cmd /c " + sRTMPDump);

               sRTMPDump = RTMP_DUMP_PATH + sRTMPDumpCmd;

//               System.out.println(sRTMPDump);

               oPattern = Pattern.compile(PRG_PATTERN);

               String cmd[] =
               {
                        "cmd",
                        "/c",
                        sRTMPDump 
               };

               oRTMPDumpThread = new RTMPDumpThread(cmd);
               oRTMPDumpThread.start();

            } 
            catch(UnsupportedEncodingException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch(IOException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }             
         }
         else
         {
            bIsStarted = false;
            btnGet.setIcon(new ImageIcon(SlaviShowRTMPDump.class.getResource("/icons/play.png")));
            vStop();
         }

      }
   }

   @Override
   public void flavorsChanged(FlavorEvent fe)
   {
      Transferable trans = oClipboard.getContents(null);
      if(trans.isDataFlavorSupported(DataFlavor.stringFlavor)) 
      {
          try 
          {
              // cast to string
              String s = (String) trans.getTransferData(DataFlavor.stringFlavor);
              if(s.contains(DOMAIN))
                 txtURL.setText(s);
              // only StringSelection can take ownership, i think
//              StringSelection ss = new StringSelection(s);
              // set content, take ownership
//              cb.setContents(ss, ss);
          } 
          catch (UnsupportedFlavorException e) 
          {
              e.printStackTrace();
          }
          catch (IOException e) 
          {
              e.printStackTrace();
          }
      }         }

   private String getFromURL(String sURL)
   {
      final String USER_AGENT = "Mozilla/5.0";

      String sResponse = null;

      URL oURL;

      BufferedReader in;

      HttpURLConnection oHTTPConn;

      try
      {
         // String sURLEncoded = URLEncoder.encode(sURL, "UTF-8");

         oURL = new URL(sURL);
         oHTTPConn = (HttpURLConnection) oURL.openConnection();

         // optional default is GET
         oHTTPConn.setRequestMethod("GET");

         // add reuqest header
         oHTTPConn.setRequestProperty("User-Agent", USER_AGENT);

         if(oHTTPConn.getResponseCode() == 200)
         {
            in = new BufferedReader(new InputStreamReader(
                     oHTTPConn.getInputStream(), "UTF-8"));
            // in = new BufferedReader(new
            // InputStreamReader(oHTTPConn.getInputStream()));

            String inputLine;
            StringBuffer sbResponse = new StringBuffer();

            while((inputLine = in.readLine()) != null)
               sbResponse.append(inputLine + "\n");
            // int inputChar;
            // while ((inputChar = in.read()) != -1)
            // sbResponse.append((char)inputChar);
            // sResponse = sbResponse.toString();
            // txtSong.setText(sResponse);
            in.close();

            sResponse = sbResponse.toString();

            // // Get song title and author
            // int iTtlNmBgn = sResponse.indexOf(sTitleNameBgn);
            // int iTtlNmEnd = sResponse.indexOf(sTitleNameEnd);
            // String sTtlNm = sResponse.substring(iTtlNmBgn +
            // sTitleNameBgn.length(), iTtlNmEnd);
            //
            // // Get and set song title
            // int iTtlBgn = sTtlNm.indexOf(sTitleBgn);
            // int iTtlEnd = sTtlNm.indexOf(sAuthorBgn);
            // sTitle = sTtlNm.substring(iTtlBgn + sTitleBgn.length(), iTtlEnd);
            //
            // // Get and set song author
            // int iAthEnd = sTtlNm.indexOf(sAuthorEnd);
            // sAuthor = sTtlNm.substring(iTtlEnd + sAuthorBgn.length(),
            // iAthEnd);
            //
            // // Get and set song text
            // int iTxtBgn = sResponse.indexOf(sTextBgn);
            // int iTxtEnd = sResponse.indexOf(sTextEnd);
            // sSong = sResponse.substring(iTxtBgn + sTextBgn.length(),
            // iTxtEnd);
         }
      } catch(MalformedURLException e)
      {
         e.printStackTrace();
      } catch(ProtocolException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch(IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      taCommand.setText(sResponse);

      return sResponse;
   }

   private String cyr2lat(String sCyr)
   {
      String[] listLat = lat.split(" ");
      String[] listCyr = cyr.split(" ");

      String sLat = sCyr;
      for(int i = 0; i < listCyr.length; i++)
      {
         sLat = sLat.replaceAll(listCyr[i], listLat[i]);
      }

      return sLat;
   }

   class StreamGobbler extends Thread
   {
      InputStream  is;
      String       type;
      OutputStream os;
      int          iPrgPos = -1,
                   iLen;

      StreamGobbler(InputStream is, String type)
      {
         this(is, type, null);
      }

      StreamGobbler(InputStream is, String type, OutputStream redirect)
      {
         this.is = is;
         this.type = type;
         this.os = redirect;
      }

      public void run()
      {
         try
         {
            PrintWriter pw = null;
            if(os != null)
               pw = new PrintWriter(os);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while((line = br.readLine()) != null)
            {
               oMatcher = oPattern.matcher(line);

               if(oMatcher.matches())
               {
                  if(iPrgPos == -1)
                     iPrgPos = taCommand.getDocument().getLength();
                  else
                     taCommand.getDocument().remove(iPrgPos, taCommand.getDocument().getLength() - iPrgPos);
               }

               if(pw != null)
                  pw.println(line);
//               System.out.println(((type == null || type.isEmpty()) ? "" : type + ">")
//                                  + line);
               taCommand.append("\n" 
                                + ((type == null || type.isEmpty()) ? "" : type + ">")
                                + line);
               iLen = taCommand.getDocument().getLength();
               taCommand.setCaretPosition(iLen);               
            }
            if(pw != null)
               pw.flush();
         } 
         catch(IOException ioe)
         {
            ioe.printStackTrace();
         } 
         catch(BadLocationException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   class RTMPDumpThread extends Thread
   {
      InputStream  is;
      String       type;
      OutputStream os;

      String[]     cmd;
      
      StreamGobbler errorGobbler, outputGobbler;

      Process p;
      
      RTMPDumpThread(String[] cmd)
      {
         this.cmd = cmd;
      }

      public void run()
      {
         try
         {
            p = Runtime.getRuntime().exec(cmd);

            // any error message?
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), null);

            // any output?
            // StreamGobbler(p.getInputStream(), "OUTPUT", fos);
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), null);

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            p.waitFor();
//            int exitVal = p.waitFor();
//            System.out.println("ExitValue: " + exitVal);
         } 
         catch(IOException ioe)
         {
            ioe.printStackTrace();
         } 
         catch(InterruptedException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }


   private Clipboard getSystemClipboard() 
   {  
       oClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();  
       oClipboard.addFlavorListener(this);  
       if(oClipboard == null)  
       {  
           System.err.println("clipboard is null");  
           return oClipboard;  
       }  
       else  
       {  
           return oClipboard;  
       }  
   }
   
   private void vStop()
   {
      try
      {
         Runtime.getRuntime().exec("taskkill /F /IM rtmpdump.exe");
      } 
      catch(IOException e)
      {
         e.printStackTrace();
      }      
   }
}
