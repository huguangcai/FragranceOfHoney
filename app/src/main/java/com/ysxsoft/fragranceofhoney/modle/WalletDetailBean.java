package com.ysxsoft.fragranceofhoney.modle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletDetailBean {

    /**
     * code : 0
     * msg : 成功！！
     * data : [{"uid":28,"falgs":"-0","type":2,"values":"尾号6668银行卡提现","addtime":"2019-01-20 15:50:03"},{"uid":28,"falgs":"-510","type":2,"values":"尾号6668银行卡提现","addtime":"2019-01-20 15:53:00"},{"uid":28,"falgs":"-510","type":2,"values":"尾号6668银行卡提现","addtime":"2019-01-21 09:35:35"}]
     *  : 1
     */

    private String code;
    private String msg;
    private int last_page;
    private List<DataBean> data;


    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

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
         * uid : 28
         * falgs : -0
         * type : 2
         * values : 尾号6668银行卡提现
         * addtime : 2019-01-20 15:50:03
         */

        private int uid;
        private String falgs;
        private int type;
        private String values;
        private String addtime;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getFalgs() {
            return falgs;
        }

        public void setFalgs(String falgs) {
            this.falgs = falgs;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getValues() {
            return values;
        }

        public void setValues(String values) {
            this.values = values;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
