package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class GetGoodsAddressBean {
    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"aid":2,"is_ture":0,"provice":"河南省","city":"郑州市","area":"高新区","address":"西四环莲花街交叉口向北600米","linkname":"李","phone":"15138651630"},{"aid":3,"is_ture":0,"provice":"河南省","city":"郑州市","area":"高新区","address":"西四环莲花街交叉口向北300米","linkname":"李","phone":"15138651630"}]
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
         * aid : 2
         * is_ture : 0
         * provice : 河南省
         * city : 郑州市
         * area : 高新区
         * address : 西四环莲花街交叉口向北600米
         * linkname : 李
         * phone : 15138651630
         */

        private int aid;
        private int is_ture;
        private String provice;
        private String city;
        private String area;
        private String address;
        private String linkname;
        private String phone;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getIs_ture() {
            return is_ture;
        }

        public void setIs_ture(int is_ture) {
            this.is_ture = is_ture;
        }

        public String getProvice() {
            return provice;
        }

        public void setProvice(String provice) {
            this.provice = provice;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLinkname() {
            return linkname;
        }

        public void setLinkname(String linkname) {
            this.linkname = linkname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
