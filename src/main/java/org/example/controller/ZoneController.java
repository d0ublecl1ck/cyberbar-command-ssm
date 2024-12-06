package org.example.controller;

import org.example.entity.Zone;
import org.example.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {
    @Autowired
    private ZoneService zoneService;
    
    @GetMapping
    public ResponseEntity<List<Zone>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(@PathVariable Integer id) {
        Zone zone = zoneService.getZoneById(id);
        if (zone != null) {
            return ResponseEntity.ok(zone);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Integer> createZone(@RequestBody Zone zone) {
        return ResponseEntity.ok(zoneService.createZone(zone));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateZone(@PathVariable Integer id, @RequestBody Zone zone) {
        zone.setId(id);
        return ResponseEntity.ok(zoneService.updateZone(zone));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteZone(@PathVariable Integer id) {
        return ResponseEntity.ok(zoneService.deleteZone(id));
    }
} 