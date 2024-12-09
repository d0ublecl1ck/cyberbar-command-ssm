package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.entity.Zone;
import org.example.service.ZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 区域管理控制器
 */
@Api(tags = "区域管理接口")
@RestController
@RequestMapping("/api/zones")
public class ZoneController {
    private static final Logger logger = LoggerFactory.getLogger(ZoneController.class);
    
    @Autowired
    private ZoneService zoneService;
    
    /**
     * 获取所有区域
     * @return 区域列表的响应实体
     */
    @GetMapping
    @ApiOperation("获取所有区域")
    public ResponseEntity<List<Zone>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }
    
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取区域")
    public ResponseEntity<Zone> getZoneById(@PathVariable Integer id) {
        Zone zone = zoneService.getZoneById(id);
        if (zone != null) {
            return ResponseEntity.ok(zone);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    @ApiOperation("创建新区域")
    public ResponseEntity<Map<String, String>> createZone(@RequestBody Zone zone) {
        Map<String, String> response = new HashMap<>();
        if (zone.getName() == null || zone.getName().trim().isEmpty()) {
            response.put("message", "区域名称不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        zoneService.createZone(zone);
        response.put("message", "创建成功");
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("更新区域信息")
    public ResponseEntity<Map<String, String>> updateZone(@PathVariable Integer id, @RequestBody Zone zone) {
        Map<String, String> response = new HashMap<>();
        if (zone.getName() == null || zone.getName().trim().isEmpty()) {
            response.put("message", "区域名称不能为空");
            return ResponseEntity.badRequest().body(response);
        }
        zone.setId(id);
        int result = zoneService.updateZone(zone);
        if (result > 0) {
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        }
        response.put("message", "区域不存在");
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除区域")
    public ResponseEntity<Map<String, String>> deleteZone(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        int result = zoneService.deleteZone(id);
        if (result > 0) {
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        }
        response.put("message", "区域不存在");
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/check/{name}")
    @ApiOperation("检查区域名称是否已存在")
    public ResponseEntity<Map<String, Object>> checkZoneName(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        
        // 参数检查
        if (name == null || name.trim().isEmpty()) {
            logger.info("区域名称为空");
            response.put("message", "区域名称不能为空");
            response.put("exists", true);  // 空名称不可用
            return ResponseEntity.badRequest().body(response);
        }
        
        // 去除空格
        String trimmedName = name.trim();
        logger.info("开始检查区域名称: [{}]", trimmedName);
        
        // 查询
        Zone existingZone = zoneService.getZoneByName(trimmedName);
        boolean exists = existingZone != null;
        
        logger.info("区域名称 [{}] 查询结果: {}", trimmedName, exists ? "已存在" : "不存在");
        logger.info("existingZone: {}", existingZone);  // 打印完整对象信息
        
        // 设置响应
        response.put("exists", exists);
        response.put("message", exists ? "区域名称已存在" : "区域名称可用");
        
        // 打印最终响应
        logger.info("响应内容: {}", response);
        
        return ResponseEntity.ok(response);
    }
} 