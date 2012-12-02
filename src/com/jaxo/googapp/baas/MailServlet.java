/*
* $Id: MailServlet.java,v 1.4 2012-04-16 15:53:11 pgr Exp $
*
* (C) Copyright 2011-2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 01/26/2012
*/
package com.jaxo.googapp.baas;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jaxo.image.RasterImage;

@SuppressWarnings("serial")
/*-- class MailServlet --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: MailServlet.java,v 1.4 2012-04-16 15:53:11 pgr Exp $
*/
public class MailServlet extends HttpServlet
{
   /*-------------------------------------------------------------------doGet-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {
      doPost(request, response);
   }

   /*------------------------------------------------------------------doPost-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public void doPost(HttpServletRequest request, HttpServletResponse resp)
   throws IOException
   {
      Logger logger = Logger.getLogger(
         "com.jaxo.googapp.baas.MailServlet"
      );
      Session session = Session.getDefaultInstance(new Properties(), null);
      try {
         MimeMessage rcvd = new MimeMessage(session, request.getInputStream());
         Address[] addresses = rcvd.getReplyTo();
         if (addresses.length > 0) {
            MailResponseHolder response = new MailResponseHolder(
               session, addresses
            );
            try {
               String demand;
               MailPieces pieces = new MailPieces(rcvd);
               RasterImage image = pieces.getAttachedImage();
               if (image != null) {
                  demand = "decode";
                  Utils.decode(image, "0", "0", response);
               }else {
                  // logger.info(request.getRequestURI());
                  byte[] keyParam = pieces.getTextBody();
                  if (keyParam != null) {
                     demand = "encode";
                     // TODO: allow type, size to be passed in the request
                     Utils.encode(
                        keyParam,
                        1,     // type
                        null,  // attributes (String)
                        'M',   // size
                        false, // isBase64
                        response
                     );
                  }else {
                     demand = "invalid";
                     response.setInfos(
                        "** Missing: text to encode, or image to decode"
                     );
                  }
               }
               response.send();
               logger.info(
                  "Replied to " + demand +
                  " request from " + addresses[0].toString()
               );
            }catch (Exception e) {
               response.setInfos(e.getMessage());
               response.send();
               logger.warning(
                  e.toString() + " while replying to " +
                  addresses[0].toString()
               );
            }
         }else {
            logger.warning("Cant'answer email request: noone to reply to.");
         }
      }catch (Exception e) {
         logger.severe(Utils.getTrace(e));
      }
   }
}
/*===========================================================================*/
