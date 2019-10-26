package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class MessageListBean {
    /**
     * code : 0
     * msg : 获取成功
     * data : [{"id":989,"types":0,"addtime":"2019-10-24 11:02:31","flag":1,"texts":"标题","news":1,"user_ids":"86,869,191,177,189,","text":"标题"},{"id":990,"types":0,"addtime":"2019-10-24 11:03:14","flag":1,"texts":"标题1","news":1,"user_ids":"86,286,177,191,","text":"标题1"},{"id":991,"types":0,"addtime":"2019-10-24 11:08:10","flag":1,"texts":"标题2","news":1,"user_ids":"86,189,177,","text":"标题2"},{"id":992,"types":0,"addtime":"2019-10-24 11:30:23","flag":1,"texts":"标题3","news":1,"user_ids":"86,186,191,177,","text":"标题3"},{"id":994,"types":2,"addtime":"2019-10-24 16:08:45","flag":2,"texts":"","news":1,"user_ids":"","list":[{"goods_name":"【货号：8017】【香芊蜜韵】新款连帽拉链卫衣长袖纯色加绒情侣装休闲上衣印制LOGO校服广告服","goods_img":"http://xiangqianmiyun.ysxapp.com/uploads///20190929/e578b63f1093821000d75171a10f2c31.jpg","size":"S","colour":"红色"}],"text":""},{"id":997,"types":0,"addtime":"2019-10-25 15:23:22","flag":1,"texts":"标题4","news":1,"user_ids":"189,86,177,191,","text":"标题4"},{"id":998,"types":2,"addtime":"2019-10-25 16:51:06","flag":2,"texts":"","news":1,"user_ids":"","list":[{"goods_name":"【货号：8011】【香芊蜜韵】专柜热卖休闲纯色卫衣圆领长袖情侣装舒适透气时尚上衣印制LOGO校服文化衫","goods_img":"http://www.xiangqianmiyun.com/uploads/20190929/792887ebcad6e3e964ce92aff06882f4.jpg","size":"S","colour":"红色"}],"text":""},{"id":999,"types":2,"addtime":"2019-10-25 16:54:08","flag":2,"texts":"","news":1,"user_ids":"","list":[{"goods_name":"【货号：8015】【香芊蜜韵】新款休闲套头加绒卫衣情侣装韩版修身男女上衣舒适透气可印制LOGO","goods_img":"http://www.xiangqianmiyun.com/uploads/20190925/3681a6ae7d04352be549eec4ffc645ee.jpg","size":"S","colour":"红色"},{"goods_name":"【货号：8011】【香芊蜜韵】专柜热卖休闲纯色卫衣圆领长袖情侣装舒适透气时尚上衣印制LOGO校服文化衫","goods_img":"http://www.xiangqianmiyun.com/uploads/20190929/792887ebcad6e3e964ce92aff06882f4.jpg","size":"S","colour":"红色"}],"text":""},{"id":1000,"types":0,"addtime":"2019-10-25 18:06:11","flag":1,"texts":"标题5","news":1,"user_ids":"189,86,177,","text":"标题5"}]
     * last_page : 1
     */

    private int code;
    private String msg;
    private int last_page;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 989
         * types : 0
         * addtime : 2019-10-24 11:02:31
         * flag : 1
         * texts : 标题
         * news : 1
         * user_ids : 86,869,191,177,189,
         * text : 标题
         * list : [{"goods_name":"【货号：8017】【香芊蜜韵】新款连帽拉链卫衣长袖纯色加绒情侣装休闲上衣印制LOGO校服广告服","goods_img":"http://xiangqianmiyun.ysxapp.com/uploads///20190929/e578b63f1093821000d75171a10f2c31.jpg","size":"S","colour":"红色"}]
         */

        private String id;
        private String types;
        private String addtime;
        private int flag;
        private String texts;
        private int news;
        private String user_ids;
        private String text;
        private List<ListBean> list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getTexts() {
            return texts;
        }

        public void setTexts(String texts) {
            this.texts = texts;
        }

        public int getNews() {
            return news;
        }

        public void setNews(int news) {
            this.news = news;
        }

        public String getUser_ids() {
            return user_ids;
        }

        public void setUser_ids(String user_ids) {
            this.user_ids = user_ids;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * goods_name : 【货号：8017】【香芊蜜韵】新款连帽拉链卫衣长袖纯色加绒情侣装休闲上衣印制LOGO校服广告服
             * goods_img : http://xiangqianmiyun.ysxapp.com/uploads///20190929/e578b63f1093821000d75171a10f2c31.jpg
             * size : S
             * colour : 红色
             */

            private String goods_name;
            private String goods_img;
            private String size;
            private String colour;

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_img() {
                return goods_img;
            }

            public void setGoods_img(String goods_img) {
                this.goods_img = goods_img;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getColour() {
                return colour;
            }

            public void setColour(String colour) {
                this.colour = colour;
            }
        }
    }


//    /**
//     * code : 0
//     * data : [{"addtime":"2019-10-24 11:02:31","flag":1,"id":989,"news":1,"text":"<p>新版本升级啦！<\/p>","types":0},{"addtime":"2019-10-24 11:03:14","flag":1,"id":990,"news":1,"text":"<p>新版本又升级了<\/p>","types":0},{"addtime":"2019-10-24 11:08:10","flag":1,"id":991,"news":1,"text":"<p>ssssssssssss<\/p>","types":0},{"addtime":"2019-10-24 11:30:23","flag":1,"id":992,"news":1,"text":"<p>消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息消息<\/p>","types":0},{"addtime":"2019-10-24 16:08:45","flag":2,"id":994,"list":[{"colour":"红色","goods_img":"http://xiangqianmiyun.ysxapp.com/uploads///20190929/e578b63f1093821000d75171a10f2c31.jpg","goods_name":"【货号：8017】【香芊蜜韵】新款连帽拉链卫衣长袖纯色加绒情侣装休闲上衣印制LOGO校服广告服","size":"S"}],"news":0,"text":"","types":2}]
//     * last_page : 1
//     * msg : 获取成功
//     */
//
//    private int code;
//    private int last_page;
//    private String msg;
//    private List<DataBean> data;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public int getLast_page() {
//        return last_page;
//    }
//
//    public void setLast_page(int last_page) {
//        this.last_page = last_page;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        /**
//         * addtime : 2019-10-24 11:02:31
//         * flag : 1
//         * id : 989
//         * news : 1
//         * text : <p>新版本升级啦！</p>
//         * types : 0
//         * list : [{"colour":"红色","goods_img":"http://xiangqianmiyun.ysxapp.com/uploads///20190929/e578b63f1093821000d75171a10f2c31.jpg","goods_name":"【货号：8017】【香芊蜜韵】新款连帽拉链卫衣长袖纯色加绒情侣装休闲上衣印制LOGO校服广告服","size":"S"}]
//         */
//
//        private String addtime;
//        private int flag;
//        private String id;
//        private int news;
//        private String text;
//        private String types;
//        private List<ListBean> list;
//
//        public String getAddtime() {
//            return addtime;
//        }
//
//        public void setAddtime(String addtime) {
//            this.addtime = addtime;
//        }
//
//        public int getFlag() {
//            return flag;
//        }
//
//        public void setFlag(int flag) {
//            this.flag = flag;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public int getNews() {
//            return news;
//        }
//
//        public void setNews(int news) {
//            this.news = news;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//
//        public String getTypes() {
//            return types;
//        }
//
//        public void setTypes(String types) {
//            this.types = types;
//        }
//
//        public List<ListBean> getList() {
//            return list;
//        }
//
//        public void setList(List<ListBean> list) {
//            this.list = list;
//        }
//
//        public static class ListBean {
//            /**
//             * colour : 红色
//             * goods_img : http://xiangqianmiyun.ysxapp.com/uploads///20190929/e578b63f1093821000d75171a10f2c31.jpg
//             * goods_name : 【货号：8017】【香芊蜜韵】新款连帽拉链卫衣长袖纯色加绒情侣装休闲上衣印制LOGO校服广告服
//             * size : S
//             */
//
//            private String colour;
//            private String goods_img;
//            private String goods_name;
//            private String size;
//
//            public String getColour() {
//                return colour;
//            }
//
//            public void setColour(String colour) {
//                this.colour = colour;
//            }
//
//            public String getGoods_img() {
//                return goods_img;
//            }
//
//            public void setGoods_img(String goods_img) {
//                this.goods_img = goods_img;
//            }
//
//            public String getGoods_name() {
//                return goods_name;
//            }
//
//            public void setGoods_name(String goods_name) {
//                this.goods_name = goods_name;
//            }
//
//            public String getSize() {
//                return size;
//            }
//
//            public void setSize(String size) {
//                this.size = size;
//            }
//        }
//    }

//
//    /**
//     * code : 0
//     * msg : 成功！
//     * data : [{"sid":2,"goods_name":"阿迪达斯男装","goods_img":"http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg","size":"M","colour":"红色","flag":2,"types":1,"addtime":"2019-01-22 14:03:02","news":1},{"sid":1,"flag":1,"text":"1478","addtime":"2019-01-21 11:40:08","news":1}]
//     */
//
//    private String code;
//    private String msg;
//    private int last_page;
//    private List<DataBean> data;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public int getLast_page() {
//        return last_page;
//    }
//
//    public void setLast_page(int last_page) {
//        this.last_page = last_page;
//    }
//
//    public static class DataBean {
//        /**
//         * sid : 2
//         * goods_name : 阿迪达斯男装
//         * goods_img : http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg
//         * size : M
//         * colour : 红色
//         * flag : 2
//         * types : 1
//         * addtime : 2019-01-22 14:03:02
//         * news : 1
//         * text : 1478
//         */
//
//        private String sid;
//        private String goods_name;
//        private String goods_img;
//        private String size;
//        private String colour;
//        private int flag;
//        private String types;
//        private String addtime;
//        private int news;
//        private String text;
//
//        public String getSid() {
//            return sid;
//        }
//
//        public void setSid(String sid) {
//            this.sid = sid;
//        }
//
//        public String getGoods_name() {
//            return goods_name;
//        }
//
//        public void setGoods_name(String goods_name) {
//            this.goods_name = goods_name;
//        }
//
//        public String getGoods_img() {
//            return goods_img;
//        }
//
//        public void setGoods_img(String goods_img) {
//            this.goods_img = goods_img;
//        }
//
//        public String getSize() {
//            return size;
//        }
//
//        public void setSize(String size) {
//            this.size = size;
//        }
//
//        public String getColour() {
//            return colour;
//        }
//
//        public void setColour(String colour) {
//            this.colour = colour;
//        }
//
//        public int getFlag() {
//            return flag;
//        }
//
//        public void setFlag(int flag) {
//            this.flag = flag;
//        }
//
//        public String getTypes() {
//            return types;
//        }
//
//        public void setTypes(String types) {
//            this.types = types;
//        }
//
//        public String getAddtime() {
//            return addtime;
//        }
//
//        public void setAddtime(String addtime) {
//            this.addtime = addtime;
//        }
//
//        public int getNews() {
//            return news;
//        }
//
//        public void setNews(int news) {
//            this.news = news;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//    }
}
