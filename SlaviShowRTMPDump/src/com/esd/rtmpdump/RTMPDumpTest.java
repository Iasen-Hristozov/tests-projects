package com.esd.rtmpdump;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class RTMPDumpTest
{
   private final static String RTMP_DUMP_PATH = "D:\\Iasen\\Documents\\Tools\\rtmpdump-2.4-git-010913-windows\\",
                               RTMP_DUMP_CMD = "rtmpdump.exe -v -r \"rtmp://video.slavishow.com/slavishow/\" -a \"slavishow/\" -f \"WIN 13,0,0,214\" -W \"http://www.slavishow.com/content/themes/slavishow/swf/flowplayer.commercial-3.2.18.swf\" -p \"%s\" -y \"slavishow/%s\" --tcUrl \"rtmp://video.slavishow.com/slavishow/\" -R --buffer 2000 -o \"D:\\%s.flv\"",
                               sURLEnc = "http://www.slavishow.com/%D0%BA%D1%80%D0%B8%D1%81%D0%B8%D1%8F-%D1%82%D0%BE%D0%B4%D0%BE%D1%80%D0%BE%D0%B2%D0%B0-simply-the-best/",
                               sMP4 = "20140406_KRISIA_SIMLPY_THE_BEST.mp4",
                               sNameLat = "20140406_krisiya-todorova-simply-the-best";   

   public static void main(String[] args)
   {
//      try {
//            String line;
//            /**
//             * getRunTime() Returns the runtime object associated with the current
//             * Java application. Most of the methods of class Runtime are instance
//             * methods and must be invoked with respect to the current runtime object.
//             *
//             * exec() method Executes the specified string command in a separate
//             * process. The command argument is parsed into tokens and then
//             * executed as a command in a separate process. The token
//             * parsing is done by a java.util.StringTokenizer created by the call.
//             *
//             */
//            Process p = Runtime.getRuntime().exec("C:/windows/system32/ping.exe sanjaal.com -t");
// 
//            /**
//             * Create a buffered reader from the Process' input stream.
//             */
//            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
// 
//            /**
//             * Loop through the input stream to print the program output into console.
//             */
//            while ((line = input.readLine()) != null) 
//            {
//                System.out.println(line);
//            }
//            /**
//             * Finally close the reader
//             */
//            input.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }      
      
      try {
         String line;
         
//         String[] cmd = {"cmd", "/c", RTMP_DUMP_PATH + "test.bat aaa", "sss" };
//         String[] cmd = {"cmd", "/c", RTMP_DUMP_PATH + "rtmpdumpSlavi.bat", sURLEnc, sMP4, sNameLat };
         String cmd[] = {"cmd", "/c",  RTMP_DUMP_PATH + String.format(RTMP_DUMP_CMD, sURLEnc, sMP4, sNameLat)};
         
//         String[] cmd = {RTMP_DUMP_PATH + "rtmpdumpSlavi1.bat", "http://www.slavishow.com/%D0%BA%D1%80%D0%B8%D1%81%D0%B8%D1%8F-%D1%82%D0%BE%D0%B4%D0%BE%D1%80%D0%BE%D0%B2%D0%B0-simply-the-best/"};

         FileOutputStream fos = new FileOutputStream(args[0]);
         Process p = Runtime.getRuntime().exec(cmd);

//         Process p = Runtime.getRuntime().exec(RTMP_DUMP_PATH + "test.bat aaa");
//         Process p = Runtime.getRuntime().exec("C:/windows/system32/ping.exe sanjaal.com -t");
//         BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
//         BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//         while ((line = bri.readLine()) != null) 
//         {
//           System.out.println(line);
//         }
//         bri.close();
//         while ((line = bre.readLine()) != null) {
//           System.out.println(line);
//         }
//         bre.close();
//         p.waitFor();
//         System.out.println("Done.");
         
         // any error message?
         StreamGobbler errorGobbler = new 
             StreamGobbler(p.getErrorStream(), "");            
         
         // any output?
//         StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT", fos);

         StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
         
         
         // kick them off
         errorGobbler.start();
         outputGobbler.start();
                                 
         // any error???
         int exitVal = p.waitFor();
         System.out.println("ExitValue: " + exitVal);
         fos.flush();
         fos.close();             
       }
       catch (Exception err) {
         err.printStackTrace();
       }      
      
//      try {
//         String line;
//         Process p = Runtime.getRuntime().exec("cmd /c dir");
//         p.waitFor();
//         System.out.println(p.exitValue());
//       }
//       catch (Exception err) {
//         err.printStackTrace();
//       }      
      
      
      
//      Process command;
//      ProcessBuilder pb = new ProcessBuilder(RTMP_DUMP_PATH + "test.bat", "http://www.slavishow.com/%D0%BA%D1%80%D0%B8%D1%81%D0%B8%D1%8F-%D1%82%D0%BE%D0%B4%D0%BE%D1%80%D0%BE%D0%B2%D0%B0-simply-the-best/");
//      
//      try
//      {
//         command = pb.start();
//         InputStream stderr = command.getErrorStream();
//         InputStreamReader isr = new InputStreamReader(stderr);
//         BufferedReader br = new BufferedReader(isr);
//         String sline = null;
//         System.out.println("<ERROR>");
//         while ( (sline = br.readLine()) != null)
//             System.out.println(sline);
//         System.out.println("</ERROR>");
//         int exitVal = command.waitFor();
//         System.out.println("Process exitValue: " + exitVal);   
//         
//      } catch(IOException e)
//      {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      } catch(InterruptedException e)
//      {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }

   }


   
}

class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    OutputStream os;
    
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
            if (os != null)
                pw = new PrintWriter(os);
                
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            {
                if (pw != null)
                    pw.println(line);
                System.out.println(type + (type.isEmpty() ? "" : ">") + line);    
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe)
            {
            ioe.printStackTrace();  
            }
    }
}   
