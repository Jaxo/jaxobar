/*
* $Id: HttpEncodeServlet.java,v 1.4 2012-04-12 06:47:39 pgr Exp $
*
* (C) Copyright 2011-2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 12/15/2011
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
/*-- class HttpEncodeServlet --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: HttpEncodeServlet.java,v 1.4 2012-04-12 06:47:39 pgr Exp $
*/
public class HttpEncodeServlet extends HttpServlet
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
   @SuppressWarnings("rawtypes")
   public void doPost(
      HttpServletRequest request,
      HttpServletResponse resp
   )
   throws IOException
   {
      //*/ Logger logger = Logger.getLogger(
      //*/   "com.jaxo.googapp.baas.HttpEncodeServlet"
      //*/ );
      Map table = request.getParameterMap();
      ResponseHolder response = new HttpResponseHolder(resp);
      byte[] data = Utils.getBytes(table, "KEY");
      if (data.length == 0) {
         response.setInfos("[no data to encode]");
         response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }else {
         try {
            /*
            | KEY is the data to encode
            | TYP has the symbology (see com.jaxo.midp.extbarcode.Bar)
            | MOD is any modification (symbo dependent)
            | SIZ is the module size
            */
            Utils.encode(
               Utils.getBytes(table, "KEY"),
               Integer.parseInt(Utils.getValue(table, "TYP", "1")),
               Utils.getValue(table, "MOD", null),
               Utils.getValue(table, "SIZ", "M").charAt(0),
               Utils.getValue(table, "B64", "0").equals("1"),
               response
            );
         }catch (Exception e) {
            response.setInfos(e.getMessage());
//*/        response.setInfos(Utils.getTrace(e));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //*/ logger.log(Level.SEVERE, Utils.getTrace(e));
            //*/ e.printStackTrace(os);
         }
      }
   }
}
/*===========================================================================*/
