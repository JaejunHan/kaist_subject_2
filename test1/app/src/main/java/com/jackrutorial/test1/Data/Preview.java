package com.jackrutorial.test1.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Preview {
//    private String id;
    private String name;
    private String title;
    private String subtitle;
    private String content;
    private String score;
    private Date date;
    private String dateToStr;

    public void setName(String name){
        this.name = name;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setSubtitle(String subtitle){
        this.subtitle = subtitle;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setScore(String score){
        this.score = score;
    }

    public void setDate(String dateToStr){
        this.dateToStr = dateToStr;
    }

//    public String getId()
//    {
//        return id;
//    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public String getContent()
    {
        return content;
    }

    public String getScore()
    {
        return score;
    }

    public String getDateToStr() {
        return dateToStr;
    }
}
