/*
* $Id: Constants.java,v 1.26 2012-11-26 09:03:40 pgr Exp $
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

/*-- class Constants --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: Constants.java,v 1.26 2012-11-26 09:03:40 pgr Exp $
*/
public class Constants {
   static public final String appTitle = "Barcode as a Service";
   static public final String appVersion = "alpha, v0.8.03";
   static public final String copyright = "&copy; 2011-2012 Jaxo, Inc.";

   static public final String emailSender = "baas@jaxo.com";

   static public final String[] barNames = {
      // see com.jaxo.extbarcode.Bar
      "?",                         //  0
      "Datamatrix",                //  1
      "QrCode",                    //  2
      "PDF 417",                   //  3
      "Aztec",                     //  4
      "EAN 13",                    //  5
      "EAN 8",                     //  6
      "Code 11",                   //  7
      "Code 25",                   //  8
      "Code 25 Interleaved",       //  9
      "Code 39",                   // 10
      "Code 39 Extended",          // 11
      "Code 93",                   // 12
      "Code 93 Extended",          // 13
      "Code 128",                  // 14
      "UCC 128",                   // 15
      "UPC-A",                     // 16
      "UPC-E",                     // 17
      "Codabar",                   // 18
      "PostNet",                   // 19
      "Micro QrCode"               // 20
   };

   static public final boolean[] isDecodable = {
      false,                       //  0  "?",
      true,                        //  1  "Datamatrix",
      true,                        //  2  "QrCode",
      true,                        //  3  "PDF 417",
      false,                       //  4  "Aztec",
      true,                        //  5  "EAN 13",
      true,                        //  6  "EAN 8",
      false,                       //  7  "Code 11",
      false,                       //  8  "Code 25",
      false,                       //  9  "Code 25 Interleaved",
      true,                        // 10  "Code 39",
      false,                       // 11  "Code 39 Extended",
      false,                       // 12  "Code 93",
      false,                       // 13  "Code 93 Extended",
      false,                       // 14  "Code 128",
      false,                       // 15  "UCC 128",
      false,                       // 16  "UPC-A",
      false,                       // 17  "UPC-E",
      false,                       // 18  "Codabar",
      false,                       // 19  "PostNet",
      false,                       // 20  "Micro QrCode"
   };
}
/*===========================================================================*/
