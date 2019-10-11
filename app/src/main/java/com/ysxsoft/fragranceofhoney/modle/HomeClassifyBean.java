package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class HomeClassifyBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"id":9,"brand_name":"路易威登","img":"22","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/23536b226c6954c84a70bb4f2a3b00b9.jpg"},{"id":8,"brand_name":"李宁","img":"21","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/5ef2d089148b66bb590e3e369ca0c1f0.jpg"},{"id":7,"brand_name":"耐克","img":"20","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/90aaabf7d821caae91c69a103b88f1eb.jpg"},{"id":6,"brand_name":"圣罗兰","img":"19","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/8c1a127efb6b479091373f9545d3fece.jpg"},{"id":5,"brand_name":"迪奥","img":"17","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/c4ebb8f0dc2b2d561427ee8b1146159a.jpg"},{"id":4,"brand_name":"SWATCH斯沃淇","img":"16","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/eae9e47bc3cf7e40ea41ebf853b5271f.jpg"},{"id":3,"brand_name":"新百伦","img":"7","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190102/a48a499e5264b82ea98864b8a3195d35.jpg"},{"id":1,"brand_name":"阿迪达斯","img":"3","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20181228/9f34b0a9cb07f5b69de0d780b1ca91bd.jpg"}]
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
         * id : 9
         * brand_name : 路易威登
         * img : 22
         * imgurl : http://bzdsh.sanzhima.cn/uploads/images/20190104/23536b226c6954c84a70bb4f2a3b00b9.jpg
         */

        private int id;
 //        private String brand_name;
        private String category_name;
        private String img;
        private String imgurl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
//        public String getBrand_name() {
//            return brand_name;
//        }
//
//        public void setBrand_name(String brand_name) {
//            this.brand_name = brand_name;
//        }
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
