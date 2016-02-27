package com.dap.qcd.data;

import com.avos.avoscloud.AVObject;
import com.dap.qcd.util.DapLog;
import org.json.JSONObject;

/**
 * Created by dap on 16/1/4.
 */
public class Home {

    private String id;//发帖id
    private String title;//标题
    private String detail;//摘要
    private int reply_num;//回复数
    private int repost_num;//转贴数
    private String content;
    private String type;//类型
    private int good;//赞
    private String time;//时间
    private String url;
    private User user;
    private AVObject infos;


    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public AVObject getInfos() {
        return infos;
    }

    public void setInfos(AVObject infos) {
        this.infos = infos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReply_num() {
        return reply_num;
    }

    public void setReply_num(int reply_num) {
        this.reply_num = reply_num;
    }

    public int getRepost_num() {
        return repost_num;
    }

    public void setRepost_num(int repost_num) {
        this.repost_num = repost_num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void toObj(JSONObject json){
        title=json.optString("title");
        detail=json.optString("content");
        time=json.optString("time");
        url=json.optJSONObject("pic_left").optString("url");

        DapLog.e(json.optString("doc_type")+json.optJSONObject("pic_left").toString());
    }

}
