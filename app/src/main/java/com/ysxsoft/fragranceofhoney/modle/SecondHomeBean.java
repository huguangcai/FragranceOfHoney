package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class SecondHomeBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":11,"brand_name":"女士职业装","img":"72","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190126/b18ee84c6f456edc566042f69ab33025.jpg"},{"id":2,"brand_name":"阿迪达斯","img":"3","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20181228/9f34b0a9cb07f5b69de0d780b1ca91bd.jpg"}]
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
         * id : 11
         * brand_name : 女士职业装
         * img : 72
         * imgurl : http://bzdsh.sanzhima.cn/uploads/images/20190126/b18ee84c6f456edc566042f69ab33025.jpg
         */

        private int id;
        private String brand_name;
        private String img;
        private String imgurl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
