package com.ysxsoft.fragranceofhoney.modle;

public class LoginBean {

    /**
     * code : 0
     * msg : 登陆成功！
     * userinfo : {"uid":28,"avatar":"http://bzdsh.sanzhima.cn/static/admin/img/avatar.jpg"}
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
         * uid : 28
         * avatar : http://bzdsh.sanzhima.cn/static/admin/img/avatar.jpg
         */

        private String uid;
        private String avatar;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
