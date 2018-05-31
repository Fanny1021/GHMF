package com.fanny.ghmf.util;

/**
 * Created by Fanny on 17/12/13.
 */

public interface ApiUrls {
    /** 主机 */
    String HOST = "http://192.168.1.110:8080/";
    /** 首页 */
    String HOME = HOST + "home";
    /** 应用 */
    String APP = HOST + "app";
    /** 游戏 */
    String GAME = HOST + "game";
    /** 专题 */
    String SUBJECT = HOST + "subject";
    /** 推荐 */
    String RECOMMEND = HOST + "recommend";
    /** 分类 */
    String CATEGORY = HOST + "category";
    /** 排行 */
    String TOP = HOST + "top";
    /** 图片 */
    String IMAGE = HOST + "image";
    /** 详情页 */
    String DETAIL = HOST + "detail";
    /** 下载 */
    String DOWNLOAD = HOST + "download";

    String TESTJSON = HOST +"fanweb/getjson.action";
}
