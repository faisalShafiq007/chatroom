package com.genericplanet.fbcat.classes;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room {

    public ArrayList<String> participants;
    public String desc,title;
    public Date time;

    public Room(ArrayList<String> participants, String desc, String title) {
        this.participants = participants;
        this.desc = desc;
        this.title=title;
        time=new Date();
    }
}
