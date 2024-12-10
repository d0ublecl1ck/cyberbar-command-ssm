package org.example.mapper;

import org.example.entity.Commodity;
import org.example.dto.CommodityQueryDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CommodityMapper {
    List<Commodity> selectAll();
    Commodity selectById(Integer id);
    int insert(Commodity commodity);
    int update(Commodity commodity);
    int deleteById(Integer id);
    List<Commodity> selectByCondition(CommodityQueryDTO queryDTO);
    Commodity selectByName(String name);
} 