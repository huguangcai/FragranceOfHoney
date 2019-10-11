package com.ysxsoft.fragranceofhoney.modle;

public class SystemDetialBean {


    /**
     * code : 0
     * msg : 获取成功！
     * data : {"sid":1,"flag":1,"text":"<p>147811<\/p>","addtime":"2019-01-21 11:40:08"}
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
         * sid : 1
         * flag : 1
         * text : <p>147811</p>
         * addtime : 2019-01-21 11:40:08
         */

        private int sid;
        private int flag;
        private String text;
        private String addtime;

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
