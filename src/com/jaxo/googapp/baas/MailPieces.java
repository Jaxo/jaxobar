/*
* $Id: MailPieces.java,v 1.3 2012-04-12 06:47:39 pgr Exp $
*
* (C) Copyright 2011-2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 01/29/2012
*/
package com.jaxo.googapp.baas;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import com.jaxo.image.RasterImage;

/*-- class MailPieces --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: MailPieces.java,v 1.3 2012-04-12 06:47:39 pgr Exp $
*/
class MailPieces
{
   List<Body> m_bodies;
   List<Attachment> m_attachments;

   /*--------------------------------------------------------------MailPieces-+
   *//**
   * Constructor
   *//*
   +-------------------------------------------------------------------------*/
   MailPieces(MimeMessage msg) throws Exception {
      m_bodies = new ArrayList<Body>();
      m_attachments = new ArrayList<Attachment>();
      extractPieces(msg);
   }

   /*--------------------------------------------------------getAttachedImage-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public RasterImage getAttachedImage() throws Exception
   {
      InputStream in = null;
      for (Attachment attachment: m_attachments) {
         if (attachment.m_type.startsWith("image")) {
            in = attachment.m_in;
         }
      }
      if ((in == null) && (m_attachments.size() > 0)) {
         // fallback: take 1st attachment if any
         in = m_attachments.get(0).m_in;
      }
      if (in == null) {
         return null;
      }else {
         return Utils.makeRasterImage(in);
      }
   }

   /*-------------------------------------------------------------getTextBody-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public byte[] getTextBody() {
      for (Body body: m_bodies) {
         if (body.m_type.startsWith("text/plain")) {
            return body.m_content.getBytes();
         }
      }
      return null;
   }

   /*----------------------------------------------------------------toString-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("<h4>Attachment(s)</h4><ul>");
      for (Attachment attachment: m_attachments) {
         sb.append("<li>");
         sb.append(attachment.toString());
         sb.append("</li>");
      }
      sb.append("</ul><h4>Inline(s)</h4><ul>");
      for (Body body: m_bodies) {
         sb.append("<li>");
         sb.append(body.toString());
         sb.append("</li>");
      }
      sb.append("</ul>");
      return sb.toString();
   }

   /*-----------------------------------------------------------extractPieces-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   private void extractPieces(Part msg) throws Exception
   {
      Object content = msg.getContent();
      if (content instanceof Multipart) {
         Multipart multipart = (Multipart)content;
         for (int i=0; i < multipart.getCount(); ++i) {
            extractPieces(multipart.getBodyPart(i));
         }
      }else {
         String type = msg.getContentType();
         if (content instanceof InputStream) {
            m_attachments.add(
               new Attachment(
                  type, msg.getFileName(), (InputStream)content
               )
            );
         }else if (content instanceof String) {
            m_bodies.add(new Body(type, (String)content));
         }
      }
   }

   /*------------------------------------------------------------ class Body -+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   private static class Body
   {
      private String m_type;
      private String m_content;

      Body(String type, String content) {
         m_type = type.toLowerCase();
         m_content = content;
      }
      public String toString() {
         return "Inline: " + m_content + " " + m_type;
      }
   }

   /*------------------------------------------------------ class Attachment -+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   private static class Attachment
   {
      private String m_type;
      private String m_fileName;
      private InputStream m_in;

      Attachment(String type, String fileName, InputStream in) {
         m_type = type.toLowerCase();
         m_fileName = fileName;
         m_in = in;
      }
      public String toString() {
         return ("Attachment: " + m_fileName + " " + m_type);
      }
   }
}

/*===========================================================================*/
