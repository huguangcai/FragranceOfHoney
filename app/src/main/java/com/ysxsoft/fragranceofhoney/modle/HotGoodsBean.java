package com.ysxsoft.fragranceofhoney.modle;

import java.util.List;

public class HotGoodsBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : [{"gid":36,"goods_name":"阿迪达斯女装","price":"699","imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/e27e1156231762d37385f889df89f7a7.jpg","group":0},{"gid":34,"goods_name":"李宁女鞋","price":"599","imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/ae4e9f4c0475b2bf67fa842a7662c6ec.jpg","group":0},{"gid":33,"goods_name":"耐克男鞋","price":"799","imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/7eb9e32d3617a33ed028a3d79f0a5334.jpg","group":0},{"gid":32,"goods_name":"圣罗兰","price":"699","imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/9ce03e5f3d7bc19d417a961cf146b4ec.jpg","group":0},{"gid":31,"goods_name":"迪奥","price":"269","imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg","group":0},{"gid":30,"goods_name":"斯沃琪","price":"269","imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg","group":0},{"gid":29,"goods_name":"阿迪达斯男装","price":"269","imgurl":"http://bzdsh.sanzhima.cn/uploads/20190109/aa382aff12506ffd880b9a3f9200edb4.jpg","group":0}]
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
         * gid : 36
         * goods_name : 阿迪达斯女装
         * price : 699
         * imgurl : http://bzdsh.sanzhima.cn/uploads/20190109/e27e1156231762d37385f889df89f7a7.jpg
         * group : 0
         */

        private int gid;
        private String goods_name;
        private String price;
        private String imgurl;
        private int group;

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }
    }
}
