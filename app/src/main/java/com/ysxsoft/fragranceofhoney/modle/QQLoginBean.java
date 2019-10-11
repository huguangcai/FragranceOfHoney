package com.ysxsoft.fragranceofhoney.modle;

public class QQLoginBean {

    /**
     * code : 0
     * msg : 登陆成功！
     * userinfo : {"uid":39,"avatar":"http://bzdsh.sanzhima.cn/uploads/images/20181222/eff6ff2d376211db4fec22318d8d983d.jpg","mobile_bind":0}
     */

    private String code;
    private String msg;
    private UserinfoBean userinfo;

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

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * uid : 39
         * avatar : http://bzdsh.sanzhima.cn/uploads/images/20181222/eff6ff2d376211db4fec22318d8d983d.jpg
         * mobile_bind : 0
         */

        private int uid;
        private String avatar;
        private int mobile_bind;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getMobile_bind() {
            return mobile_bind;
        }

        public void setMobile_bind(int mobile_bind) {
            this.mobile_bind = mobile_bind;
        }
    }
}
