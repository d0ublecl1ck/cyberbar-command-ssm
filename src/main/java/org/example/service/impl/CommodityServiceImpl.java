package org.example.service.impl;

import org.example.entity.Commodity;
import org.example.mapper.CommodityMapper;
import org.example.service.CommodityService;
import org.example.dto.CommodityQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;
    
    @Override
    public List<Commodity> getAllCommodities() {
        return commodityMapper.selectAll();
    }
    
    @Override
    public Commodity getCommodityById(Integer id) {
        return commodityMapper.selectById(id);
    }
    
    @Override
    public int createCommodity(Commodity commodity) {
        return commodityMapper.insert(commodity);
    }
    
    @Override
    public int updateCommodity(Commodity commodity) {
        return commodityMapper.update(commodity);
    }
    
    @Override
    public int deleteCommodity(Integer id) {
        return commodityMapper.deleteById(id);
    }
    
    @Override
    public List<Commodity> getCommoditiesByCondition(CommodityQueryDTO queryDTO) {
        return commodityMapper.selectByCondition(queryDTO);
    }
    
    @Override
    public Commodity getCommodityByName(String name) {
        return commodityMapper.selectByName(name);
    }
} 