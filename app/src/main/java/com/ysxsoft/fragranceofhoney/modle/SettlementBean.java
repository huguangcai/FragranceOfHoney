package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

/**
 * Create By 胡
 * on 2019/10/14 0014
 */
public class SettlementBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"list":[{"goods_id":230,"goods_name":"【货号：6022】【香芊蜜韵】秋冬女式无缝纯色圆领加绒保暖内衣套装修身美体秋衣秋裤","goods_img":"http://www.xiangqianmiyun.com/uploads/20190929/aacec3bf0c4382a4febfc2b8d23c7fd2.jpg,http://www.xiangqianmiyun.com/uploads/20190929/b45d2181d8f4110e3236947d75f89e06.jpg,http://www.xiangqianmiyun.com/uploads/20190929/a7187adaa796f592de8e2a0cec12705d.jpg,htt","number":"2","price":"47","size":"均码（80斤-140斤）","colour":"黑色"},{"goods_id":230,"goods_name":"【货号：6022】【香芊蜜韵】秋冬女式无缝纯色圆领加绒保暖内衣套装修身美体秋衣秋裤","goods_img":"http://www.xiangqianmiyun.com/uploads/20190929/aacec3bf0c4382a4febfc2b8d23c7fd2.jpg,http://www.xiangqianmiyun.com/uploads/20190929/b45d2181d8f4110e3236947d75f89e06.jpg,http://www.xiangqianmiyun.com/uploads/20190929/a7187adaa796f592de8e2a0cec12705d.jpg,htt","number":"1","price":"47","size":"均码（80斤-140斤）","colour":"浅灰蓝"}],"tot_price":141}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
         * list : [{"goods_id":230,"goods_name":"【货号：6022】【香芊蜜韵】秋冬女式无缝纯色圆领加绒保暖内衣套装修身美体秋衣秋裤","goods_img":"http://www.xiangqianmiyun.com/uploads/20190929/aacec3bf0c4382a4febfc2b8d23c7fd2.jpg,http://www.xiangqianmiyun.com/uploads/20190929/b45d2181d8f4110e3236947d75f89e06.jpg,http://www.xiangqianmiyun.com/uploads/20190929/a7187adaa796f592de8e2a0cec12705d.jpg,htt","number":"2","price":"47","size":"均码（80斤-140斤）","colour":"黑色"},{"goods_id":230,"goods_name":"【货号：6022】【香芊蜜韵】秋冬女式无缝纯色圆领加绒保暖内衣套装修身美体秋衣秋裤","goods_img":"http://www.xiangqianmiyun.com/uploads/20190929/aacec3bf0c4382a4febfc2b8d23c7fd2.jpg,http://www.xiangqianmiyun.com/uploads/20190929/b45d2181d8f4110e3236947d75f89e06.jpg,http://www.xiangqianmiyun.com/uploads/20190929/a7187adaa796f592de8e2a0cec12705d.jpg,htt","number":"1","price":"47","size":"均码（80斤-140斤）","colour":"浅灰蓝"}]
         * tot_price : 141
         */

        private String tot_price;
        private List<ListBean> list;

        public String getTot_price() {
            return tot_price;
        }

        public void setTot_price(String tot_price) {
            this.tot_price = tot_price;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * goods_id : 230
             * goods_name : 【货号：6022】【香芊蜜韵】秋冬女式无缝纯色圆领加绒保暖内衣套装修身美体秋衣秋裤
             * goods_img : http://www.xiangqianmiyun.com/uploads/20190929/aacec3bf0c4382a4febfc2b8d23c7fd2.jpg,http://www.xiangqianmiyun.com/uploads/20190929/b45d2181d8f4110e3236947d75f89e06.jpg,http://www.xiangqianmiyun.com/uploads/20190929/a7187adaa796f592de8e2a0cec12705d.jpg,htt
             * number : 2
             * price : 47
             * size : 均码（80斤-140斤）
             * colour : 黑色
             */

            private String goods_id;
            private String goods_name;
            private String goods_img;
            private String number;
            private String price;
            private String size;
            private String colour;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_img() {
                return goods_img;
            }

            public void setGoods_img(String goods_img) {
                this.goods_img = goods_img;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getColour() {
                return colour;
            }

            public void setColour(String colour) {
                this.colour = colour;
            }
        }
    }
}
