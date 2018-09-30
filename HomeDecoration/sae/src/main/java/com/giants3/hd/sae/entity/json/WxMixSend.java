package com.giants3.hd.sae.entity.json;

import com.giants3.hd.utils.GsonUtils;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by david on 2016/1/21.
 */
public class WxMixSend {


    /**
     * touser : OPENID
     * msgtype : news
     * news : {"articles":[{"title":"Happy Day","description":"Is Really A Happy Day","url":"URL","picurl":"PIC_URL"},{"title":"Happy Day","description":"Is Really A Happy Day","url":"URL","picurl":"PIC_URL"}]}
     */

    public static final String testString="{\"articles\":[{\"title\":\"Happy Day\",\"description\":\"Is Really A Happy Day\",\"url\":\"URL\",\"picurl\":\"PIC_URL\"},{\"title\":\"Happy Day\",\"description\":\"Is Really A Happy Day\",\"url\":\"URL\",\"picurl\":\"PIC_URL\"}]}";


    public String touser;
    public String msgtype;
    public NewsEntity news;

    public static WxMixSend objectFromData(String str) {

        return GsonUtils.fromJson(str, WxMixSend.class);
    }

    public static class NewsEntity {
        /**
         * title : Happy Day
         * description : Is Really A Happy Day
         * url : URL
         * picurl : PIC_URL
         */

        public List<ArticlesEntity> articles;

        public static NewsEntity objectFromData(String str) {

            return GsonUtils.fromJson(str, NewsEntity.class);
        }

        public static class ArticlesEntity {
            public String title;
            public String description;
            public String url;
            public String picurl;

            public static ArticlesEntity objectFromData(String str) {

                return GsonUtils.fromJson(str, ArticlesEntity.class);
            }
        }
    }
}
