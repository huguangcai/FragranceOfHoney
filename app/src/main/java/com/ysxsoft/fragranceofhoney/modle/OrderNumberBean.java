package com.ysxsoft.fragranceofhoney.modle;

/**
 * Create By 胡
 * on 2019/10/14 0014
 */
public class OrderNumberBean {


    /**
     * code : 0
     * msg : 获取成功
     * data : {"type1":6,"type2":17,"type3":2,"type4":19,"type5":0}
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
         * type1 : 6
         * type2 : 17
         * type3 : 2
         * type4 : 19
         * type5 : 0
         */

        private int type1;
        private int type2;
        private int type3;
        private int type4;
        private int type5;

        public int getType1() {
            return type1;
        }

        public void setType1(int type1) {
            this.type1 = type1;
        }

        public int getType2() {
            return type2;
        }

        public void setType2(int type2) {
            this.type2 = type2;
        }

        public int getType3() {
            return type3;
        }

        public void setType3(int type3) {
            this.type3 = type3;
        }

        public int getType4() {
            return type4;
        }

        public void setType4(int type4) {
            this.type4 = type4;
        }

        public int getType5() {
            return type5;
        }

        public void setType5(int type5) {
            this.type5 = type5;
        }
    }
}
