package com.jackrutorial.test1.Data;

public class Preview {
    private String id;
    private String name;
    private String title;
    private String content;
    private String updated;

    public Preview(String name, String title, String content){
        this.id = id;
        this.name = name;
        this.title = title;
        this.content = content;
        this.updated = updated;
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

    public String getContent()
    {
        return content;
    }

    public String getUpdated()
    {
        return updated;
    }

}
