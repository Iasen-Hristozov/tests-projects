package com.discworld.jdownloaderx.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "book")
public class Book extends CFile
{
   @XmlElement(name = "author", required = true)
   private  String sAuthor;
   @XmlElement(name = "title", required = true)
   private  String sTitle;
   @XmlElement(name = "volume", required = true)
   private  String sVolume;
   
   public Book()
   {
      super();
   }

   public Book(String sName, String sURL)
   {
      super(sName, sURL);
   }   

   public Book(String sName, String sURL, String sAuthor, String sTitle, String sVolume)
   {
      super(sName, sURL);
      this.sAuthor = sAuthor;
      this.sTitle = sTitle;
      this.sVolume = sVolume;
   }   
   
   public String getAuthor()
   {
      return sAuthor;
   }

   public void setAuthor(String sAuthor)
   {
      this.sAuthor = sAuthor;
   }

   public String getTitle()
   {
      return sTitle;
   }

   public void setTitle(String sTitle)
   {
      this.sTitle = sTitle;
   }

   public String getVolume()
   {
      return sVolume;
   }

   public void setVolume(String sVolume)
   {
      this.sVolume = sVolume;
   }
}
