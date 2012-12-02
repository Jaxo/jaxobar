/*
* $Id: TestImageServlet.java,v 1.5 2012-04-12 06:47:39 pgr Exp $
*
* (C) Copyright 2011-2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 02/04/2012
*/
package com.jaxo.googapp.baas;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

@SuppressWarnings("serial")
/*-- class TestImageServlet --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: TestImageServlet.java,v 1.5 2012-04-12 06:47:39 pgr Exp $
*/
public class TestImageServlet extends HttpServlet
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
//*/     "com.jaxo.googapp.baas.TestImageServlet"
//*/  );
      ServletFileUpload upload = new ServletFileUpload();
      upload.setSizeMax(120000);

      HttpResponseHolder response = new HttpResponseHolder(resp);
      try {
         String xformTo = "PNG";
         byte[] imageData = null;
         FileItemIterator iterator = upload.getItemIterator(request);
         while (iterator.hasNext()) {
            FileItemStream item = iterator.next();
            if (item.isFormField()) {
               if (item.getFieldName().equals("xformTo")) {
                  xformTo = Streams.asString(item.openStream());
               }
            }else {
               InputStream in = item.openStream();
               imageData = IOUtils.toByteArray(in);
               IOUtils.closeQuietly(in);
            }
         }
         fillResponse(imageData, xformTo, response);
      }catch (Exception e) {
         response.setInfos(Utils.getTrace(e));
         response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
   }

   /*------------------------------------------------------------fillResponse-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static void fillResponse(
      byte[] data, String xformTo, HttpResponseHolder response
   ) {
      ImagesService.OutputEncoding outEncoding;
      Image img = ImagesServiceFactory.makeImage(data);
      if (xformTo.equals("WEBP")) {
         outEncoding = ImagesService.OutputEncoding.WEBP;
      }else { // if (xformTo.equals("PNG")) {
         outEncoding = ImagesService.OutputEncoding.PNG;
      }
      /*
      | Call ImagesServices to format the Image in <outEncoding>.
      | We use "applyTransform", passing any transform method
      | that result in an identity transform.  Only the 3rd parameter
      | counts, that is the output format of the 1-to-1-transform.
      */
      Image xformImg = ImagesServiceFactory.getImagesService().applyTransform(
         ImagesServiceFactory.makeResize( // any dum transform
            img.getWidth(), img.getHeight()
         ),
         img, outEncoding
      );
      response.setInfos(
         "Image transformed to " +
         xformImg.getWidth() + "x" + xformImg.getHeight() +
         " " + xformTo + " format."
      );
      response.setImage(xformImg.getImageData(), true /*isBase64*/);
   }
}
/*===========================================================================*/
