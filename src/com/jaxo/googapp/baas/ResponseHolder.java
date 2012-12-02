/*
* $Id: ResponseHolder.java,v 1.3 2012-04-16 15:53:33 pgr Exp $
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

/*-- interface ResponseHolder --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: ResponseHolder.java,v 1.3 2012-04-16 15:53:33 pgr Exp $
*/
interface ResponseHolder {
   void setInfos(String infos);
   void setSymbology(String symbology);
   void setBodyText(String text);
   void setImage(byte[] imageData, boolean isBase64) throws Exception;
   void setStatus(int status);
   void setExtras(String name, String value);
}
/*===========================================================================*/

