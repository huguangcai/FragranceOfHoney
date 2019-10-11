package com.ysxsoft.fragranceofhoney.modle;

public class LauncherImgBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"image":14,"imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20190103/0b5f327bdc66cab8a5916afb950e623a.png"}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * image : 14
         * imgurl : http://bzdsh.sanzhima.cn/uploads/images/20190103/0b5f327bdc66cab8a5916afb950e623a.png
         */

        private int image;
        private String imgurl;

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
