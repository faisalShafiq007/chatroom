package com.genericplanet.fbcat.classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    public Message() {
    }

    private String content;
    private String sender;
    private Date time;
    private String img;

    public Message(String content, String sender, Date time) {
        this.content = content;
        this.sender = sender;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFormmattedTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String formattedDate  = dateFormat.format(time);
        return formattedDate;
    }
}
