package com.softdesign.devintensive.utils;

/**
 * Created by  on ...
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.design.widget.NavigationView;
import android.widget.ImageView;

import com.softdesign.devintensive.R;


/**
 * A Drawable that draws an oval with given {@link Bitmap}
 */
public class RoundedAvatarDrawable{

    public static NavigationView sView;

    public static ImageView getAvatar(NavigationView view) {
        sView = view;
        return (ImageView)sView.getHeaderView(0).findViewById(R.id.avatar);
    }

    public static void setAvatarBitmap(Bitmap avatarBitmap, NavigationView view) {
        try {
            getAvatar(view).setImageBitmap(avatarBitmap);
        } catch(Exception e) {}
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
             final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
             final Canvas canvas = new Canvas(output);


             final int color = Color.RED;
             final Paint paint = new Paint();
             final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
             final RectF rectF = new RectF(rect);


             paint.setAntiAlias(true);
             canvas.drawARGB(0, 0, 0, 0);
             paint.setColor(color);
             canvas.drawOval(rectF, paint);

             paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
             canvas.drawBitmap(bitmap, rect, rect, paint);


             bitmap.recycle();


             return output;
           }

}
