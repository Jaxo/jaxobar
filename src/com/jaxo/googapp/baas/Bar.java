/*
* $Id: Bar.java,v 1.5 2012-12-12 09:06:34 pgr Exp $
*
* (C) Copyright 2012 Jaxo Systems.  All rights reserved.
* This work contains confidential trade secrets of Jaxo Systems.
* Use, examination, copying, transfer and disclosure to others
* are prohibited, except with the express written agreement of Jaxo Systems.
*
* Author:  Pierre G. Richard
* Written: 2/20/2012
*/
package com.jaxo.googapp.baas;

import com.jaxo.image.RasterImage;
import com.jaxo.midp.barcode.BarException;
import com.jaxo.midp.barcode.BarImage;
import com.jaxo.midp.encoder.Base64;
import com.jaxo.midp.extbarcode.BarFactory;
import com.jaxo.midp.extbarcode.BmpImage;
import com.jaxo.midp.io.BitStream;

/*-- class Bar --+
*//**
*
* @author  Pierre G. Richard
* @version $Id: Bar.java,v 1.5 2012-12-12 09:06:34 pgr Exp $
*/
public class Bar
{
   private com.jaxo.midp.extbarcode.Bar m_bar;
   private BitStream m_bs; // when created form bytes data
   private byte[] m_contents;
   private String m_infos;
   private int m_type;

   /*---------------------------------------------------------------------Bar-+
   *//**
   * Constructor to create a Bar from given data
   *//*
   +-------------------------------------------------------------------------*/
   public Bar(int barType, byte[] data, String attributes) throws Exception {
      m_bar = BarFactory.makeBar(barType);
      m_bs = m_bar.setData(data, attributes);
      m_contents = data;
      m_infos = m_bar.infos();
      m_type = barType;
   }

   /*---------------------------------------------------------------------Bar-+
   *//**
   * Constructor to create a Bar from a given image and a set of symbologies.
   * Possible symbologies is given through thhe comma-delimited string "types",
   * the values of which must comply with the list given by the interface
   * "com.jaxo.extbarcode.Bar"
   *//*
   +-------------------------------------------------------------------------*/
   public Bar(String types, RasterImage raster) throws Exception
   {
      LuminousImage lums = new LuminousImage(raster);
      for (String type:types.split(",")) {
         try {
            m_type = Integer.parseInt(type);
            m_bar = BarFactory.makeBar(m_type);
            m_contents = BarFactory.decodeImage(m_bar, lums);
            if (m_contents != null) {
               m_infos = m_bar.infos();
               return;
            }
         }catch (Exception e) {
         }
      }
      m_bar = null;
      m_type = 0;
   }

   /*-------------------------------------------------------getResultingImage-+
   *//**
   * Get the image obtained after encoding the Bar data.
   *
   * If the Bar was created by decoding a (Raster) Image, this method will
   * recreate an Image from the data obtained from the original image.
   *//*
   +-------------------------------------------------------------------------*/
   public byte[] getImageBytes(int moduleWidth, boolean isBase64)
   throws Exception
   {
      if (m_bs == null) {
         m_bs = m_bar.setData(m_contents, null);
      }
      BmpImage img = BarFactory.makeImage(
         m_bs,
         m_bar,
         moduleWidth,
         20,  // margin
         BmpImage.TYPE_BLACK_AND_WHITE
      );
      if (isBase64) {
         return Base64.encode(img.getBytes());
      }else {
         return img.getBytes();
      }
   }

   /*---------------------------------------------------------getDataContents-+
   *//**
   * Get the data obtained after decoding the Bar image
   *//*
   +-------------------------------------------------------------------------*/
   public byte[] getDataContents() throws Exception {
      return m_contents;
   }

   /*-----------------------------------------------------------------getType-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public int getType() {
      return m_type;
   }

   /*-------------------------------------------------------------------infos-+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   public String infos() {
      return m_infos;
   }

   /*------------------------------------------------------------------toHtml-+
   *//**
   * Only for debug
   *//*
   +-------------------------------------------------------------------------*/
   static public String toHtml(RasterImage raster) {
      return new LuminousImage(raster).toHtml();
   }

   /*--------------------------------------------------- class LuminousImage -+
   *//**
   *//*
   +-------------------------------------------------------------------------*/
   static class LuminousImage extends BarImage
   {
      private int m_width;
      private int m_height;
      private byte[] m_luminances;
      LuminousImage(RasterImage raster) {
         m_width = raster.getWidth();
         m_height = raster.getHeight();
         m_luminances = raster.getLuminances();
         // log.println(raster.toString());
         addMargin(10);
      }

      /*------------------------------------------------------------addMargin-+
      * For those who do not respect the quiet zone around their
      * barcode images...
      *//**
      *//*
      +----------------------------------------------------------------------*/
      private void addMargin(int margin)
      {
         int width = m_width;
         int height = m_height;
         byte[] buffer = m_luminances;

         m_width += (margin << 1);
         m_height += (margin << 1);
         m_luminances = new byte[m_width * m_height];

         int tLen = m_width * margin;
         int tgt = tLen;

         java.util.Arrays.fill(m_luminances, 0, tLen, (byte)0xFF);
         for (int i=0, src=0; i < height; ++i, src += width) {
            java.util.Arrays.fill(m_luminances, tgt, tgt+margin, (byte)0xFF);
            tgt += margin;
            System.arraycopy(buffer, src, m_luminances, tgt, width);
            tgt += width;
            java.util.Arrays.fill(m_luminances, tgt, tgt+margin, (byte)0xFF);
            tgt += margin;
         }
         java.util.Arrays.fill(m_luminances, tgt, tgt+tLen, (byte)0xFF);
      }

      /*--------------------------------------------------------getLuminances-+
      *//**
      *//*
      +----------------------------------------------------------------------*/
      public byte[] getLuminances(
         byte[] luminances,
         int offset, int scanlength,
         int x, int y, int width, int height
      ) {
         /*
         | We *know* that getLuminances (issued by ImageBinarizer) takes the
         | following arguments:
         |   image.getLuminances(null, 0, width, 0, 0, width, height);
         | and this is what RasterImage's  expects.
         |
         | In theory, getLuminances() could be called with variant values,
         | but I realized today that those would never be used.  The design
         | of getLuminances() comes from an early age, when it wasn't clear
         | how BarImage implementations would do the job.
         |
         | Hence, until ImageBinarizer is fixed, make sure all is OK.
         */
         if (
            (offset != 0) ||
            (scanlength != width) ||
            (x != 0) || (y != 0) ||
            (width != m_width) || (height != m_height) ||
            (luminances != null)
         ) {
            throw new RuntimeException(
               "Unsupported arguments for \"getLuminances\""
            );
         }
         return m_luminances;
      }

      /*-------------------------------------------------------------getWidth-+
      *//**
      *//*
      +----------------------------------------------------------------------*/
      public int getWidth() {
         return m_width;
      }

      /*------------------------------------------------------------getHeight-+
      *//**
      *//*
      +----------------------------------------------------------------------*/
      public int getHeight() {
         return m_height;
      }

      /*---------------------------------------------------------------toHtml-+
      *//**
      *//*
      +----------------------------------------------------------------------*/
      public String toHtml() {
         StringBuilder sb = new StringBuilder();
         sb.append("<PRE style=\"font-family:monospace;font-size:2pt\">\n");
         for (int i=-2; i < m_width; ++i) sb.append('-');
         sb.append('\n');
         for (int i=0, ofs=0; i < m_height; i+=2, ofs += m_width) {
            sb.append('|');
            for (int j=0; j < m_width; ++j) {
               sb.append((((m_luminances[ofs++] & 0xFF) > 127)?' ' : 'X'));
            }
            sb.append("|\n");
         }
         for (int i=-2; i < m_width; ++i) sb.append('-');
         sb.append("</PRE>\n");
         return sb.toString();
      }
   }
}

/*===========================================================================*/
