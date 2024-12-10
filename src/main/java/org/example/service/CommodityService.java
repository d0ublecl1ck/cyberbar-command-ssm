package org.example.service;

import org.example.entity.Commodity;
import org.example.dto.CommodityQueryDTO;
import java.util.List;

public interface CommodityService {
    List<Commodity> getAllCommodities();
    Commodity getCommodityById(Integer id);
    int createCommodity(Commodity commodity);
    int updateCommodity(Commodity commodity);
    int deleteCommodity(Integer id);
    List<Commodity> getCommoditiesByCondition(CommodityQueryDTO queryDTO);
    Commodity getCommodityByName(String name);
} 