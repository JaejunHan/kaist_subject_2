package com.jackrutorial.test1.Data;

import java.util.Date;

public class User { //my_db
    private String id;
    private String nickname;
    private String sex;
    private String Api_Token;
    private String birth;
    private String create_date;
    private String update_date;

    public void setId(String id){
        this.id = id;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public void setApiToken(String Api_Token){
        this.Api_Token = Api_Token;
    }

    public void setBirth(String birth){
        this.birth = birth;
    }

    public void setCreateDate(String create_date){
        this.create_date = create_date;
    }

    public void setUpdateDate(String update_date){
        this.update_date = update_date;
    }

    public String getId(){
        return id;
    }

    public String getNickname()
    {
        return nickname;
    }

    public String getSex()
    {
        return sex;
    }

    public String getApiToken()
    {
        return Api_Token;
    }

    public String getBirth()
    {
        return birth;
    }

    public String getCreateDate()
    {
        return create_date;
    }

    public String getUpdateDate() {
        return update_date;
    }

}
