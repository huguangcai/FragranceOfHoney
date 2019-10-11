package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class BankCardListBean {

    /**
     * code : 0
     * msg : 操作成功!！
     * data : [{"pid":5,"uid":28,"realname":"李","idcard":"410","house":"中国银行","phone":"15638922803","number":"6222600260001076666"},{"pid":4,"uid":28,"realname":"李","idcard":"410","house":"中国银行","phone":"15638922803","number":"6222600260001072666"},{"pid":3,"uid":28,"realname":"李","idcard":"410","house":"中国银行","phone":"15638922803","number":"6222600260001072555"},{"pid":2,"uid":28,"realname":"李","idcard":"410","house":"中国银行","phone":"15638922803","number":"6222600260001072455"},{"pid":1,"uid":28,"realname":"李","idcard":"410","house":"中国银行","phone":"15638922803","number":"6222600260001076668"}]
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
         * pid : 5
         * uid : 28
         * realname : 李
         * idcard : 410
         * house : 中国银行
         * phone : 15638922803
         * number : 6222600260001076666
         */

        private int pid;
        private int uid;
        private String realname;
        private String idcard;
        private String house;
        private String phone;
        private String number;

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getHouse() {
            return house;
        }

        public void setHouse(String house) {
            this.house = house;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
