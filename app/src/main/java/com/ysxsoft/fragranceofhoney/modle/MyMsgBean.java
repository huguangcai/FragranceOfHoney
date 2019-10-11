package com.ysxsoft.fragranceofhoney.modle;

public class MyMsgBean {


    /**
     * code : 0
     * msg : 获取成功！
     * data : {"id":28,"nickname":"°","avatar":"1","sex":1,"imgurl":"http://bzdsh.sanzhima.cn/uploads/images/20181222/eff6ff2d376211db4fec22318d8d983d.jpg"}
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
         * id : 28
         * nickname : °
         * avatar : 1
         * sex : 1
         * imgurl : http://bzdsh.sanzhima.cn/uploads/images/20181222/eff6ff2d376211db4fec22318d8d983d.jpg
         */

        private int id;
        private String nickname;
        private String avatar;
        private String sex;
        private String imgurl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
