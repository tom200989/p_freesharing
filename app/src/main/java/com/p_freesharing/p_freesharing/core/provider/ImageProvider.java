package com.p_freesharing.p_freesharing.core.provider;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.p_freesharing.p_freesharing.bean.Freesharing_Image;

import java.util.ArrayList;
import java.util.List;

public class ImageProvider implements AbstructProvider {
    private Context context;

    public ImageProvider(Context context) {
        this.context = context;
    }

    @Override
    public List<?> getList() {
        List<Freesharing_Image> list = null;
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                list = new ArrayList<Freesharing_Image>();
                while (cursor.moveToNext()) {
                    
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    long date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
                    
                    Freesharing_Image audio = new Freesharing_Image(id, title, displayName, mimeType, path, size, date);
                    list.add(audio);
                }
                cursor.close();
            }
        }
        return list;
    }

}
