package cn.yiport.cart.pojo.bo;

public class CartBo {
    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 购买数量
     */
    private Integer num;

    public CartBo() {
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
