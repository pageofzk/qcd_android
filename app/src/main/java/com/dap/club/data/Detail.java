package com.dap.club.data;

import com.avos.avoscloud.AVObject;
import com.dap.club.util.DapLog;

import org.json.JSONObject;

/**
 * Created by dap on 16/1/4.
 */
public class Detail {

    private String id;//发帖id
    private String title;//标题
    private String detail;//摘要
    private int reply_num;//回复数
    private int repost_num;//转贴数
    private String content;//纯文本摘要
    private User user;
    private String type;//类型
    private String time;//时间
    private String url;
    private String img_url;//主图url
    private String html;//主内容html
    private String buy_url;//购买url
    private String from_url;//来源url



    private AVObject infos;

    public AVObject getInfos() {
        return infos;
    }

    public void setInfos(AVObject infos) {
        this.infos = infos;
    }


    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBuy_url() {
        return buy_url;
    }

    public void setBuy_url(String buy_url) {
        this.buy_url = buy_url;
    }

    public String getFrom_url() {
        return from_url;
    }

    public void setFrom_url(String from_url) {
        this.from_url = from_url;
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



}
