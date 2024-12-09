package org.example.mapper;

import org.example.entity.Zone;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 区域数据访问接口
 */
public interface ZoneMapper {
    /**
     * 查询所有区域
     * @return 区域列表
     */
    List<Zone> selectAll();
    
    /**
     * 根据ID查询区域
     * @param id 区域ID
     * @return 区域信息
     */
    Zone selectById(Integer id);
    
    /**
     * 插入区域
     * @param zone 区域信息
     * @return 插入结果
     */
    int insert(Zone zone);
    
    /**
     * 更新区域
     * @param zone 区域信息
     * @return 更新结果
     */
    int update(Zone zone);
    
    /**
     * 根据ID删除区域
     * @param id 区域ID
     * @return 删除结果
     */
    int deleteById(Integer id);
    
    /**
     * 根据名称查询区域
     * @param name 区域名称
     * @return 区域信息
     */
    Zone selectByName(@Param("name") String name);
} 