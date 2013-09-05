package com.discworld.EnglishSyllableCounter;

import javax.swing.SwingUtilities;

public class EnglishSyllableCounter
{

   /**
    * @param args
    */
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            new Interface();
         }
      });
   }

}
