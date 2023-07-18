package com.google.ar.core.examples.java.common.samplerender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

public class Text {
    public static Texture drawCanvasToTexture(SampleRender render,
                                       String aText,
                                       float aFontSize) {
        Bitmap textBitmap;
        int textcount = aText.length();
        //Texture texture = new Texture(render, Texture.Target.TEXTURE_2D, Texture.WrapMode.CLAMP_TO_EDGE);
        if (aFontSize < 8.0f)
            aFontSize = 8.0f;
        if (aFontSize > 500.0f)
            aFontSize = 500.0f;
        try {
            Paint textPaint = new Paint();
            textPaint.setTextSize(aFontSize);
            textPaint.setFakeBoldText(false);
            textPaint.setAntiAlias(true);
            textPaint.setARGB(255, 255, 255, 255);
            // If a hinting is available on the platform you are developing, you should enable it (uncomment the line below).
            //textPaint.setHinting(Paint.HINTING_ON);
            textPaint.setSubpixelText(true);
            textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
            float realTextWidth = textPaint.measureText(aText);
            // Creates a new mutable bitmap, with 128px of width and height
            int bitmapWidth = (int) (realTextWidth + 0.5f);
            int bitmapHeight = (int) ((aFontSize) + 0.5f)*textcount;
            textBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(textBitmap);
            canvas.drawText(aText,0,-textPaint.ascent(),textPaint);
            Texture texture = Texture.createFromBitmap(render, Texture.ColorFormat.SRGB, Texture.WrapMode.CLAMP_TO_EDGE, textBitmap);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getTextureId());
            // Assigns the OpenGL texture with the Bitmap
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, textBitmap, 0);
            textBitmap.recycle();
            return texture;
        } catch (Throwable t) {
            Log.e("TEXTURE",t.toString());
            return new Texture(render, Texture.Target.TEXTURE_2D, Texture.WrapMode.CLAMP_TO_EDGE);
        }
    }
}
