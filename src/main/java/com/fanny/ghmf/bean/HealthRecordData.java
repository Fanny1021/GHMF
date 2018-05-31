package com.fanny.ghmf.bean;

import java.util.List;

/**
 * Created by Fanny on 17/12/13.
 */

public class HealthRecordData {
    public List<HealthRecordInfo> list;

    public static class HealthRecordInfo {
        /**
         * title : 游戏
         * infos : [{"url1":"image/category_game_0.jpg","url2":"image/category_game_1.jpg","url3":"image/category_game_2.jpg",
         * "name1":"休闲","name2":"棋牌","name3":"益智"},
         * {"url1":"image/category_game_3.jpg",
         * "url2":"image/category_game_4.jpg",
         * "url3":"image/category_game_5.jpg",
         * "name1":"射击","name2":"体育","name3":"儿童"}]
         */

        public String title;
        public List<CategoryItem> infos;

        public static class CategoryItem {
            /**
             * url1 : image/category_game_0.jpg
             * url2 : image/category_game_1.jpg
             * url3 : image/category_game_2.jpg
             * name1 : 休闲
             * name2 : 棋牌
             * name3 : 益智
             */

            public String url1;
            public String url2;
            public String url3;
            public String name1;
            public String name2;
            public String name3;
        }
    }
}
