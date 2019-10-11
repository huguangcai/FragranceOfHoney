package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class SearchRecommendBean {

    /**
     * code : 0
     * msg : 搜索成功！
     * data : [{"count":98,"goods_name":"阿迪"},{"count":17,"goods_name":"阿迪达斯"},{"count":13,"goods_name":"李宁"},{"count":8,"goods_name":"商品名称"},{"count":4,"goods_name":"达斯"},{"count":3,"goods_name":"12"},{"count":3,"goods_name":"阿迪达斯女装"},{"count":1,"goods_name":"发给"},{"count":1,"goods_name":"全额"},{"count":1,"goods_name":"啊"}]
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
         * count : 98
         * goods_name : 阿迪
         */

        private int count;
        private String goods_name;

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
    }
}
