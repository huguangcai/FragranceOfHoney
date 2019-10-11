package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class ClassifyDataBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"pid":1,"category_name":"女装","one":[{"bid":2,"brand_name":"阿迪达斯","img":"3","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20181228/9f34b0a9cb07f5b69de0d780b1ca91bd.jpg"}]},{"pid":2,"category_name":"男装","one":[{"bid":1,"brand_name":"阿迪达斯","img":"3","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20181228/9f34b0a9cb07f5b69de0d780b1ca91bd.jpg"},{"bid":3,"brand_name":"新百伦","img":"7","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190102/a48a499e5264b82ea98864b8a3195d35.jpg"}]},{"pid":3,"category_name":"配饰","one":[{"bid":4,"brand_name":"SWATCH斯沃淇","img":"16","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/eae9e47bc3cf7e40ea41ebf853b5271f.jpg"}]},{"pid":4,"category_name":"包包","one":[{"bid":5,"brand_name":"迪奥","img":"17","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/c4ebb8f0dc2b2d561427ee8b1146159a.jpg"},{"bid":9,"brand_name":"路易威登","img":"22","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/23536b226c6954c84a70bb4f2a3b00b9.jpg"}]},{"pid":5,"category_name":"美妆","one":[{"bid":6,"brand_name":"圣罗兰","img":"19","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/8c1a127efb6b479091373f9545d3fece.jpg"}]},{"pid":6,"category_name":"男鞋","one":[{"bid":7,"brand_name":"耐克","img":"20","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/90aaabf7d821caae91c69a103b88f1eb.jpg"}]},{"pid":7,"category_name":"女鞋","one":[{"bid":8,"brand_name":"李宁","img":"21","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190104/5ef2d089148b66bb590e3e369ca0c1f0.jpg"}]}]
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
         * pid : 1
         * category_name : 女装
         * one : [{"bid":2,"brand_name":"阿迪达斯","img":"3","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20181228/9f34b0a9cb07f5b69de0d780b1ca91bd.jpg"}]
         */

        private int pid;
        private String category_name;
        private List<OneBean> one;

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public List<OneBean> getOne() {
            return one;
        }

        public void setOne(List<OneBean> one) {
            this.one = one;
        }

        public static class OneBean {
            /**
             * bid : 2
             * brand_name : 阿迪达斯
             * img : 3
             * imgurl : http://bzdsh.sanzhima.cn/uploads/images/20181228/9f34b0a9cb07f5b69de0d780b1ca91bd.jpg
             */

            private int bid;
            private String brand_name;
            private String img;
            private String imgurl;

            public int getBid() {
                return bid;
            }

            public void setBid(int bid) {
                this.bid = bid;
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
}
