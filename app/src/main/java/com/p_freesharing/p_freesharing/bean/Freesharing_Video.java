package com.p_freesharing.p_freesharing.bean;

public class Freesharing_Video {
    private int id;
    private String title;
    private String album;
    private String artist;
    private String displayName;
    private String mimeType;
    private String path;// 路径
    private long size;// 大小
    private long duration;// 时长
    private long date;// 日期

    /**
     *
     */
    public Freesharing_Video() {
        super();
    }

    /**
     * @param id
     * @param title
     * @param album
     * @param artist
     * @param displayName
     * @param mimeType
     * @param size
     * @param duration
     * @param date
     */
    public Freesharing_Video(int id, String title, String album, String artist, String displayName, String mimeType, String path, long 
                                                                                                                              size, long duration, long date) {
        super();
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.path = path;
        this.size = size;
        this.duration = duration;
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Freesharing_Video{" + "id=" + id + "\n" + " title='" + title + '\'' + "\n" + " album='" + album + '\'' + "\n" + " artist='" + artist + '\'' + "\n" + " displayName='" + displayName + '\'' + "\n" + " mimeType='" + mimeType + '\'' + "\n" + " path='" + path + '\'' + "\n" + " size=" + size + "\n" + " duration=" + duration + '}';
    }
}
