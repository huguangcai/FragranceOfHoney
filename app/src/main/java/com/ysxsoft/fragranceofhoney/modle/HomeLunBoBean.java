package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class HomeLunBoBean {


    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"pro_id":1,"gid":29,"image":"61","imgurl":"http://bzdsh.sanzhima.cn/uploads/files/20190124/0decaff5405fbb6d71506e7f15ed4af9.jpg","type":"png","type1":"http://bzdsh.sanzhima.cn/uploads/images/20190107/2af3ceb43893145baf3b572958036b66.png"},{"pro_id":2,"gid":30,"image":"13","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190103/7a7ab47a1a8e758a8042f61a846ebff7.png","type":"png","type1":"http://bzdsh.sanzhima.cn/uploads/images/20190107/2af3ceb43893145baf3b572958036b66.png"},{"pro_id":3,"gid":31,"image":"13","imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190103/7a7ab47a1a8e758a8042f61a846ebff7.png","type":"png","type1":"http://bzdsh.sanzhima.cn/uploads/images/20190107/2af3ceb43893145baf3b572958036b66.png"}]
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
         * pro_id : 1
         * gid : 29
         * image : 61
         * imgurl : http://bzdsh.sanzhima.cn/uploads/files/20190124/0decaff5405fbb6d71506e7f15ed4af9.jpg
         * type : png
         * type1 : http://bzdsh.sanzhima.cn/uploads/images/20190107/2af3ceb43893145baf3b572958036b66.png
         */

        private int pro_id;
        private int gid;
        private String image;
        private String imgurl;
        private String type;
        private String type1;

        public int getPro_id() {
            return pro_id;
        }

        public void setPro_id(int pro_id) {
            this.pro_id = pro_id;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }
    }
}
