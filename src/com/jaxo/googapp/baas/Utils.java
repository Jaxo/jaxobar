/*
* $Id: Utils.java,v 1.15 2012-11-26 09:03:41 pgr Exp $
*
* (C) Copyright 2011-2012 Jaxo Inc.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Inc.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo.
*
* Author:  Pierre G. Richard
* Written: 12/26/2011
*/
package com.jaxo.googapp.baas;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.jaxo.image.RasterImage;

/*-- class Utils --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: Utils.java,v 1.15 2012-11-26 09:03:41 pgr Exp $
*/
class Utils
{
   static private final String upcChecker = "http://www.checkupc.com/search.php?keyword=";
   static private final Pattern upcCheckerPattern = Pattern.compile(
      ".*\"(http://cf.checkupc.com/images/products/[^\"' ]*)\"(?:.*alt=.([^\"']*))?.*"
   );
   static private final String defaultTypes;
   static {
      StringBuffer sb = new StringBuffer(15);
      for (int i=0; i < Constants.isDecodable.length; ++i) {
         if (Constants.isDecodable[i]) {
            if (sb.length() > 0) sb.append(',');
            sb.append(i);
         }
      }
      defaultTypes = sb.toString();
   }

   /*------------------------------------------------------------------decode-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static void decode(
      RasterImage image,
      String types,
      String postProcesses,
      ResponseHolder response
   )
   throws Exception
   {
      if (types.equals("0")) types = defaultTypes;
      Bar bar = new Bar(types, image);
      byte[] result = bar.getDataContents();
      if (result != null) {
         String contents = new String(result);
         response.setInfos(bar.infos());
         response.setSymbology(Constants.barNames[bar.getType()]);
         response.setBodyText(contents);
         if (postProcesses.equals("P1")) {
            fetchUpcItem(contents, response);
            // FIXME: what if something goes wrong?  Decoding failed?
         }
      }else {
         throw new Exception("[Decoding failed]");
      }
   }

   /*------------------------------------------------------------fetchUpcItem-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   private static void fetchUpcItem(
      String upcValue,
      ResponseHolder response
   )
   throws Exception
   {
      BufferedReader in = new BufferedReader(
         new InputStreamReader(
            (InputStream)new URL(upcChecker + upcValue).getContent()
         )
      );
      String line;
      while ((line=in.readLine()) != null) {
         Matcher matcher = upcCheckerPattern.matcher(line);
         if (matcher.lookingAt()) {
            int size = matcher.groupCount();
            for (int i=0; i < size; ++i) {
               String value = matcher.group(i+1);
               if (value != null) {
                  if (i==0) {
                     response.setExtras("UpcImg", value);
                  }else {
                     response.setExtras("UpcDescr", value);
                  }
               }
            }
            return;  // success!
         }
      }
      throw new Exception("No info found");
   }

   /*------------------------------------------------------------------encode-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static void encode(
      byte[] data,
      int type,
      String attributes,
      char chSize,
      boolean isBase64,
      ResponseHolder response
   ) throws Exception {
      int size;
      switch (chSize) {
      case 'S': size = 2; break;
      case 'M': size = 4; break;
      case 'L': size = 6; break;
      default:  size = 4; break;
      }
      Bar bar = new Bar(type, data, attributes);
      response.setInfos(bar.infos());
      response.setImage(bar.getImageBytes(size, isBase64), isBase64);
   }

   /*----------------------------------------------------------------getValue-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   @SuppressWarnings("rawtypes")
   static String getValue(Map table, String key, String defVal) {
      String[] strings = (String[])table.get(key);
      if (strings != null) {
         return strings[0];
      }else {
         return defVal;
      }
   }

   /*----------------------------------------------------------------getBytes-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   @SuppressWarnings("rawtypes")
   static byte[] getBytes(Map table, String key) {
      String[] vals = (String[])table.get(key);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try {
         for (String val : vals) out.write(val.getBytes());
      }catch (Exception e) {}
      return out.toByteArray();
   }

   /*---------------------------------------------------------makeRasterImage-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static RasterImage makeRasterImage(InputStream in) throws Exception {
      byte[] data = IOUtils.toByteArray(in);
      IOUtils.closeQuietly(in);
      return makeRasterImage(data);
   }

   /*---------------------------------------------------------makeRasterImage-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static RasterImage makeRasterImage(String urlString) throws Exception {
      return makeRasterImage(
         URLFetchServiceFactory.getURLFetchService().fetch(
            new URL(urlString)
         ).getContent()
      );
      /*
      URL url = new URL(urlString);
      url.openConnection();
      InputStream in = url.openStream();
      byte[] data = IOUtils.toByteArray(in);
      IOUtils.closeQuietly(in);
      init(data);
      */
   }

   /*---------------------------------------------------------makeRasterImage-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   private static RasterImage makeRasterImage(byte[] data) throws Exception
   {
      Image img = ImagesServiceFactory.makeImage(data);
      switch (img.getFormat()) {
      case WEBP:
         return RasterImage.Factory.makeWebPRaster(ByteBuffer.wrap(data));
      case TIFF:
         return RasterImage.Factory.makeTiffRaster(data);
      default:
         /*
         | Call ImagesServices to format the Image in PNG.
         | We use "applyTransform", passing any transform method
         | that result in an identity transform.  Only the 3rd parameter
         | counts, that is the output format of the 1-to-1-transform.
         */
         data = ImagesServiceFactory.getImagesService().applyTransform(
            ImagesServiceFactory.makeResize( // any dum transform
               img.getWidth(), img.getHeight()
            ),
            img, ImagesService.OutputEncoding.PNG
         ).getImageData();
         /* fall thru */
      case PNG:
         return RasterImage.Factory.makePngRaster(new ByteArrayInputStream(data));
      }
   }

   /*----------------------------------------------------------------getTrace-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static String getTrace(Throwable e) {
      ByteArrayOutputStream outB = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(outB);
      e.printStackTrace(out);
      return new String(outB.toByteArray());
   }

   /*--------------------------------------------------------------debugImage-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static void debugImage(
      RasterImage raster,
      Exception e,
      ResponseHolder response
   ) {
      if (raster != null) {
         response.setBodyText(Bar.toHtml(raster));
      }else {
         response.setBodyText(getTrace(e));
      }
   }
}
/*===========================================================================*/
