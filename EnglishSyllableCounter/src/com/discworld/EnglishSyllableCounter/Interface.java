package com.discworld.EnglishSyllableCounter;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Interface extends JFrame implements ActionListener
{
   private JPanel panel1;
   private JLabel label1;
   private JTextField field;
   private JPanel panel2;
   private JLabel label2;
   private JTextArea area;
   
   public Interface()
   {
      super("EnglishSyllableCounterTest");
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      Container container = this.getContentPane();
      
      panel1 = new JPanel();
      panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
      label1 = new JLabel("Word:");
      panel1.add(label1);
      field = new JTextField(10);
      field.addActionListener(this);
      panel1.add(field);
      
      container.add(panel1, BorderLayout.PAGE_START);
      
      {
         panel2 = new JPanel();
         panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
         label2 = new JLabel("Sylables count");
         panel2.add(label2);
         area = new JTextArea(5, 10);
         JScrollPane scroll = new JScrollPane(area,
                                              JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                              JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
         panel2.add(scroll);
      }
      
      container.add(panel2, BorderLayout.CENTER);
      
      this.setSize(300,200);
      this.setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent arg0)
   {
      String sWord = field.getText();
      
      CEnglishSyllableCounter oEnglishSyllableCounter = new CEnglishSyllableCounter();
      int iSyllablesNbr = oEnglishSyllableCounter.countSyllables(sWord);

//      area.append(sWord);
      
      area.append(sWord + " - " + String.valueOf(iSyllablesNbr) + "\n");
      field.setText(null);
   }

}
