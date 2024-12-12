package org.example.mapper;

import org.example.entity.Package;
import org.example.dto.PackageQueryDTO;

import java.util.List;

public interface PackageMapper {
    int insertPackage(Package pkg);
    int deletePackage(int userId);
    int updatePackage(Package pkg);
    List<Package> selectPackage(PackageQueryDTO queryDTO);
} 