package cn.yiport.item.bo;

import cn.yiport.item.pojo.Sku;
import cn.yiport.item.pojo.Spu;
import cn.yiport.item.pojo.SpuDetail;
import cn.yiport.item.pojo.Stock;

import java.util.List;

public class SpuBo extends Spu {

    private String cname;// 商品分类名称

    private String bname;// 品牌名称

    private SpuDetail spuDetail;

    private List<Sku> skus;

    private Stock stock;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public String getCname() {
        return cname;
    }


    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
