/*
* $Id: HttpDecodeImageServlet.java,v 1.2 2012-12-12 11:20:37 pgr Exp $
*
* (C) Copyright 2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 12/12/2012
*/
package com.jaxo.googapp.baas;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//*/ import java.util.logging.Level;
//*/ import java.util.logging.Logger;

@SuppressWarnings("serial")
/*-- class HttpDecodeImageServlet --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: HttpDecodeImageServlet.java,v 1.2 2012-12-12 11:20:37 pgr Exp $
*/
public class HttpDecodeImageServlet extends HttpServlet
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
//*/     "com.jaxo.googapp.baas.HttpDecodeImageServlet"
//*/  );
      HttpResponseHolder response = new HttpResponseHolder(resp);
      response.enableCors(request.getHeader("origin"));
      try {
         String types = "0";
         String postProcesses = "P0";
         for (
            @SuppressWarnings("unchecked")
            Enumeration<String> names = request.getParameterNames();
            names.hasMoreElements();
         ) {
            String name = names.nextElement();
            String values = request.getParameterValues(name)[0];
            if (name.equals("TYP")) {
               types = values;
            }else if (name.equals("PPC")) {
               postProcesses = values;
            }
         }
         Utils.decode(
            Utils.makeRasterImage(request.getInputStream()),
            types, postProcesses,
            response
         );
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
