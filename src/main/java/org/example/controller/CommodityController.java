package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.dto.CommodityQueryDTO;
import org.example.entity.Commodity;
import org.example.service.CommodityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.math.BigDecimal;

@Api(tags = "商品管理接口")
@RestController
@RequestMapping("/api/commodities")
public class CommodityController {
    private static final Logger logger = LoggerFactory.getLogger(CommodityController.class);
    
    @Autowired
    private CommodityService commodityService;
    
    @ApiOperation("搜索商品")
    @GetMapping("/search")
    public ResponseEntity<PageInfo<Commodity>> searchCommodities(
            @ApiParam("商品名称") @RequestParam(required = false) String name,
            @ApiParam("最低价格") @RequestParam(required = false) BigDecimal minPrice,
            @ApiParam("最高价格") @RequestParam(required = false) BigDecimal maxPrice,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        logger.info("搜索商品，筛选条件：name={}, price={}-{}, pageNum={}, pageSize={}", 
                name, minPrice, maxPrice, pageNum, pageSize);
        
        CommodityQueryDTO queryDTO = new CommodityQueryDTO();
        queryDTO.setName(name);
        queryDTO.setMinPrice(minPrice);
        queryDTO.setMaxPrice(maxPrice);
        
        PageHelper.startPage(pageNum, pageSize);
        List<Commodity> commodities = commodityService.getCommoditiesByCondition(queryDTO);
        
        return ResponseEntity.ok(new PageInfo<>(commodities));
    }
    
    @ApiOperation("获取所有商品")
    @GetMapping
    public ResponseEntity<List<Commodity>> getAllCommodities() {
        return ResponseEntity.ok(commodityService.getAllCommodities());
    }
    
    @ApiOperation("根据ID获取商品")
    @GetMapping("/{id}")
    public ResponseEntity<Commodity> getCommodityById(
            @ApiParam(value = "商品ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(commodityService.getCommodityById(id));
    }
    
    @ApiOperation("创建商品")
    @PostMapping
    public ResponseEntity<Integer> createCommodity(@RequestBody Commodity commodity) {
        return ResponseEntity.ok(commodityService.createCommodity(commodity));
    }
    
    @ApiOperation("更新商品")
    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateCommodity(
            @ApiParam(value = "商品ID", required = true) @PathVariable Integer id,
            @RequestBody Commodity commodity) {
        commodity.setId(id);
        return ResponseEntity.ok(commodityService.updateCommodity(commodity));
    }
    
    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteCommodity(
            @ApiParam(value = "商品ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(commodityService.deleteCommodity(id));
    }
} 