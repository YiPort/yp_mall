package cn.yiport.item.api;

import cn.yiport.common.pojo.PageResult;
import cn.yiport.item.bo.SpuBo;
import cn.yiport.item.pojo.Sku;
import cn.yiport.item.pojo.Spu;
import cn.yiport.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    /**
     * 根据spuId查询spuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuBoByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue ="1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    );

    /**
     * 根据spuId查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long id);


    @GetMapping("sku/{id}")
    Sku querySkuById(@PathVariable("id") Long skuId);
}

