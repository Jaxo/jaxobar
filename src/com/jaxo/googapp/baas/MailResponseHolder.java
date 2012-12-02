/*
* $Id: MailResponseHolder.java,v 1.3 2012-04-16 15:52:37 pgr Exp $
*
* (C) Copyright 2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 01/27/2012
*/
package com.jaxo.googapp.baas;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/*-- class MailResponseHolder --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: MailResponseHolder.java,v 1.3 2012-04-16 15:52:37 pgr Exp $
*/
class MailResponseHolder
implements ResponseHolder
{
   static final String ENCODING_OPTIONS = "text/html; charset=UTF-8";
   private MimeMessage m_response;
   private StringBuilder m_sb;
   private byte[] m_imageData;

   MailResponseHolder(Session session, Address[] recipients)
   throws Exception
   {
      m_sb = new StringBuilder();
      m_response = new MimeMessage(session);
      m_response.setFrom(
         new InternetAddress(Constants.emailSender, "BAAS Engine")
      );
      m_response.addRecipient(
         Message.RecipientType.TO, (InternetAddress)recipients[0]
      );
      m_response.setSubject("BAAS Service Outcome");
   }

   void send() throws Exception
   {
      if (m_imageData == null) {
         m_response.setHeader("Content-Type", ENCODING_OPTIONS);
         m_response.setContent(m_sb.toString(), ENCODING_OPTIONS);
      }else {
         Multipart parts = new MimeMultipart();
         MimeBodyPart htmlPart = new MimeBodyPart();
         MimeBodyPart imagePart = new MimeBodyPart();

         htmlPart.setContent(m_sb.toString(), "text/html");
         imagePart.setFileName(
            new SimpleDateFormat("yyyyMMdd_HHmmss'.bmp'").format(new Date())
         );
         imagePart.setDisposition(Part.ATTACHMENT);
         imagePart.setDataHandler(
            new DataHandler(
               new ByteArrayDataSource(m_imageData, "image/x-ms-bmp")
            )
         );
         parts.addBodyPart(htmlPart);
         parts.addBodyPart(imagePart);
         m_response.setContent(parts);
      }
      Transport.send(m_response);
   }

   public void setInfos(String infos) {
      m_sb.append(
         "<BR/><FONT size='2'>Infos:<BR/>" + infos + "</FONT>"
      );
   }

   public void setSymbology(String symbology) {
      m_sb.append(
         "<BR/><FONT size='2'>Symbology: " + symbology + "</FONT>"
      );
   }

   public void setExtras(String name, String value) {
      m_sb.append(
         "<BR/><FONT size='2'>" + name +": " + value + "</FONT>"
      );
   }

   public void setBodyText(String text) {
      m_sb.append(
         "<BR/><FONT size='2'>Contents:</FONT>" +
         "<BR/><FONT size='4'><B>" + text + "</B></FONT>"
      );
   }

   public void setImage(byte[] imageData, boolean isBase64) throws Exception {
      // assume it's a BMP image
      m_imageData = imageData;
   }

   public void setStatus(int status) {
      // Unused
   }
}

/*===========================================================================*/
