/*
* $Id: HttpDecodeFileServlet.java,v 1.7 2012-11-26 09:03:41 pgr Exp $
*
* (C) Copyright 2011-2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 12/24/2011
*/
package com.jaxo.googapp.baas;

import java.io.IOException;
//*/ import java.util.logging.Level;
//*/ import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.jaxo.image.RasterImage;

@SuppressWarnings("serial")
/*-- class HttpDecodeFileServlet --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: HttpDecodeFileServlet.java,v 1.7 2012-11-26 09:03:41 pgr Exp $
*/
public class HttpDecodeFileServlet extends HttpServlet
{
   /*-------------------------------------------------------------------doGet-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public void doGet(
      HttpServletRequest request,
      HttpServletResponse response
   )
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
//*/  Logger logger = Logger.getLogger(
//*/     "com.jaxo.googapp.baas.HttpDecodeFileServlet"
//*/  );
      ServletFileUpload upload = new ServletFileUpload();
      upload.setSizeMax(120000);

      RasterImage image = null;
      HttpResponseHolder response = new HttpResponseHolder(resp);
      response.enableCors(request.getHeader("origin"));
      try {
         String types = "0";
         String postProcesses = "P0";
         FileItemIterator iterator = upload.getItemIterator(request);
         while (iterator.hasNext()) {
            FileItemStream item = iterator.next();
            if (item.isFormField()) {
               String name = item.getFieldName();
               String values = Streams.asString(item.openStream());
               if (name.equals("TYP")) {
                  types = values;
               }else if (name.equals("PPC")) {
                  postProcesses = values;
               }
            }else {
               image = Utils.makeRasterImage(item.openStream());
            }
         }
         Utils.decode(image, types, postProcesses, response);
      }catch (Exception e) {
         response.setInfos(e.getMessage());
//*/     response.setInfos(Utils.getTrace(e));
//*/     if (true) {
//*/        Utils.debugImage(image, e, response);
//*/        response.setStatus(HttpServletResponse.SC_OK);
//*/     }else
         response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
   }
}

/*===========================================================================*/
