/*
* $Id: HttpResponseHolder.java,v 1.4 2012-04-16 15:52:04 pgr Exp $
*
* (C) Copyright 2011-2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 01/27/2012
*/
package com.jaxo.googapp.baas;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/*-- class HttpResponseHolder --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: HttpResponseHolder.java,v 1.4 2012-04-16 15:52:04 pgr Exp $
*/
class HttpResponseHolder implements ResponseHolder
{
   HttpServletResponse m_response;

   HttpResponseHolder(HttpServletResponse response) {
      m_response = response;
      response.setContentType("text/plain");
   }
   // Cross Origin Resource Sharing
   public void enableCors(String origin) {
      if (origin == null) {
         origin = "*";
         // throw new Exception("Invalid CORS header (no origin)");
      }
      m_response.setHeader("Access-Control-Allow-Origin", origin);
      // need cookies for session id's:
      m_response.setHeader("Access-Control-Allow-Credentials", "true");
      // m_response.setHeader("Access-Control-Expose-Headers", "Jaxo-Infos");
   }
   public void setInfos(String infos) {
      m_response.setHeader("Jaxo-Infos", infos);
   }
   public void setSymbology(String symbology) {
      m_response.setHeader("Jaxo-Symbo", symbology);
   }
   public void setExtras(String name, String value) {
      m_response.setHeader("Jaxo-" + name, value);
   }
   public void setBodyText(String text) {
      try {
         m_response.getWriter().println(text);
      }catch (IOException e) {}
   }
   public void setImage(byte[] imageData, boolean isBase64) {
      // assume it's a BMP image
      try {
         if (isBase64) {
            m_response.setHeader("Content-Transfer-Encoding", "base64");
         }else {
            m_response.setContentType("image/bmp");
         }
         m_response.getOutputStream().write(imageData);
      }catch (IOException e) {}
   }
   public void setStatus(int status) {
      m_response.setStatus(status);
   }
}
/*===========================================================================*/
