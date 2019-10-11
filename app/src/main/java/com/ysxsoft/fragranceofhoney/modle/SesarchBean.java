package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class SesarchBean {

    /**
     * code : 0
     * msg : 搜索成功！
     * data : [{"imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/e27e1156231762d37385f889df89f7a7.jpg","gid":36,"goods_name":"阿迪达斯女装","price":"699","group":0},{"imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg","gid":29,"goods_name":"阿迪达斯男装","price":"269","group":0}]
     * last_page : 1
     */

    private String code;
    private String msg;
    private int last_page;
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
         * imgurl : http://bzdsh.sanzhima.cn/uploads/20190109/e27e1156231762d37385f889df89f7a7.jpg
         * gid : 36
         * goods_name : 阿迪达斯女装
         * price : 699
         * group : 0
         */

        private String imgurl;
        private int gid;
        private String goods_name;
        private String price;
        private int group;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
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
