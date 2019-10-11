package com.ysxsoft.fragranceofhoney.modle;

public class AliPayBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : alipay_sdk=alipay-sdk-php-20161101&app_id=+2019010462832081&biz_content=%7B%22body%22%3A%22%5Cu8d2d%5Cu4e70%5Cu5546%5Cu54c1%5Cuff1a%5Cu82b1%5Cu8d395980.00%5Cu5143%22%2C%22subject%22%3A%22%5Cu8d2d%5Cu4e70%5Cu5546%5Cu54c1%5Cuff1a%5Cu82b1%5Cu8d395980.00%5Cu5143%22%2C%22out_trade_no%22%3A%222019011418224915474613692751%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%225980.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fbzdsh.sanzhima.cn%2Fadmin.php%2Fapi%2Findex%2FalipayRecharge&sign_type=RSA2×tamp=2019-01-16+17%3A15%3A10&version=1.0&sign=I6AdhSo2LjgUUIBNKnUXy0n3zTTUtt9xiQZ9vfF2F7m0EONBC3hDCodqyDuKOPPvz4m04kYbM4xwst1CGrnxMWkCQN54BPhiJzyrN0fDZBl8dHZE529oCRDeuFCR0EiN38iBXFGKWgqpZVK%2F%2FwADy%2FLLfjzyzdLmPpcWSaGplxGcnrf6PcImxhgkUHZhW26sZys%2F65eVQ1KV1PUEhuzNsLRU7AxYtXIfYVupDQKXBYFDEsk5hNmheOtOmtenNVnuMzu7%2BL2kjGC9gRkk4n5v3lHrG5gjYdh4w5AHSPJscBHk8YEqGeodCLdPZ2GoPoaxFNbmlxjjWzSXGz3XhcWPzg%3D%3D
     */

    private String code;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
