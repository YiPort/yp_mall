package cn.yiport.item.service;

import cn.yiport.item.mapper.SpecParamMapper;
import cn.yiport.item.mapper.SpecGroupMapper;
import cn.yiport.item.pojo.SpecGroup;
import cn.yiport.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupsByCid( Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.groupMapper.select(specGroup);
    }
    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupsWithParam(Long cid) {

        // 查询规格组
        List<SpecGroup> groups = this.queryGroupsByCid(cid);
        groups.forEach(g -> {
            // 查询组内参数
            g.setParams(this.queryParams(g.getId(), null, null, null));
        });
        return groups;
    }


    /**
     * 根据条件获取规格参数
     * @param gid
     * @param cid
     * @param searching
     * @param generic
     * @return
     */
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam=new SpecParam();

        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);

        return this.specParamMapper.select(specParam);
    }



}
