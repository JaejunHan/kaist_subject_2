package com.jackrutorial.test1.Data;

public class Comment {

    private String CommNickname; // 댓글 단 사람의 id
    private String CommTitle; // 댓글 본문의 title
    private String CommSubtitle; // 댓글 본문의 subtitle
    private String CommContent; // 댓글 내용
    private String CommTime; // 댓글 단 시간
    private String CommUpdateTime; // 댓글 수정 시간

    public void setCommNickname(String commNickname) {
        this.CommNickname = commNickname;
    }

    public void setCommTitle(String commTitle) {
        this.CommTitle = commTitle;
    }

    public void setCommSubtitle(String commSubtitle) {
        this.CommSubtitle = commSubtitle;
    }

    public void setCommContent(String commContent) {
        this.CommContent = commContent;
    }

    public void setCommTime(String commTime) {
        this.CommTime = commTime;
    }

    public void setCommUpdateTime(String commUpdateTime) {
        this.CommUpdateTime = commUpdateTime;
    }

    public String getCommNickname() {
        return CommNickname;
    }

    public String getCommTitle() {
        return CommTitle;
    }

    public String getCommSubtitle() {
        return CommSubtitle;
    }

    public String getCommContent() {
        return CommContent;
    }

    public String getCommTime() {
        return CommTime;
    }

    public String getCommUpdateTime() {
        return CommUpdateTime;
    }
}
