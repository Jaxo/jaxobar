/*
* $Id: HttpDecodeUrlServlet.java,v 1.7 2012-11-26 09:03:41 pgr Exp $
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
import java.util.Map;
//*/ import java.util.logging.Level;
//*/ import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
/*-- class HttpDecodeUrlServlet --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: HttpDecodeUrlServlet.java,v 1.7 2012-11-26 09:03:41 pgr Exp $
*/
public class HttpDecodeUrlServlet extends HttpServlet
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
   @SuppressWarnings("rawtypes")
   public void doPost(HttpServletRequest request, HttpServletResponse resp)
   throws IOException
   {
//*/  Logger logger = Logger.getLogger(
//*/     "com.jaxo.googapp.baas.HttpDecodeUrlServlet"
//*/  );
      HttpResponseHolder response = new HttpResponseHolder(resp);
      try {
         Map table = request.getParameterMap();
         Utils.decode(
            Utils.makeRasterImage(Utils.getValue(table, "URL", "")),
            Utils.getValue(table, "TYP", "0"),
            Utils.getValue(table, "PPC", "P0"),
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
