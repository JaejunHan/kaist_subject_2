package com.jackrutorial.test1.Data;

public class Preview {
    private String id;
    private String name;
    private String title;
    private String subtitle;
    private String content;

    public Preview(String name, String title, String content){
        this.id = id;
        this.name = name;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }

    public String getId()
    {
        return id;
    }

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

}
