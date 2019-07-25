package com.p_freesharing.p_freesharing.core.provider;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.p_freesharing.p_freesharing.bean.Freesharing_Audio;

import java.util.ArrayList;
import java.util.List;

public class AudioProvider implements AbstructProvider {

    private Context context;
    
    public AudioProvider(Context context) {
        this.context = context;
    }

    @Override
    public List<?> getList() {
        List<Freesharing_Audio> list = null;
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                list = new ArrayList<Freesharing_Audio>();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    long date = cursor
                                        .getLong(cursor
                                                         .getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));
                    Freesharing_Audio freesharingAudio = new Freesharing_Audio(id, title, album, artist, path,
                            displayName, mimeType, duration, size,date);
                    list.add(freesharingAudio);
                }
                cursor.close();
            }
        }
        return list;
    }

}
