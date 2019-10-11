package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class SearchHistoryBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"count":10,"goods_name":"阿迪达斯","uid":28},{"count":8,"goods_name":"商品名称","uid":28},{"count":4,"goods_name":"达斯","uid":28},{"count":2,"goods_name":"阿迪","uid":28},{"count":1,"goods_name":"李宁","uid":28},{"count":1,"goods_name":"耐克","uid":28},{"count":1,"goods_name":"斯沃琪","uid":28}]
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
         * count : 10
         * goods_name : 阿迪达斯
         * uid : 28
         */

        private int count;
        private String goods_name;
        private int uid;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
