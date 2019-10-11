package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class CollectsListBean {

    /**
     * code : 0
     * msg : 操作成功!！
     * data : [{"id":1,"uid":28,"gid":29,"del":0,"imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg","name":"阿迪达斯男装","price":"299","group":2}]
     * last_page : 1
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * uid : 28
         * gid : 29
         * del : 0
         * imgurl : http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg
         * name : 阿迪达斯男装
         * price : 299
         * group : 2
         */

        private int id;
        private int uid;
        private int gid;
        private int del;
        private String imgurl;
        private String name;
        private String price;
        private int group;
        private boolean isChoosed;


        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public int getDel() {
            return del;
        }

        public void setDel(int del) {
            this.del = del;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }
    }
}
