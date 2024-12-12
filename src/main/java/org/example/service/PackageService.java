package org.example.service;

import org.example.entity.Package;
import org.example.dto.PackageQueryDTO;
import com.github.pagehelper.PageInfo;

public interface PackageService {
    boolean addPackage(Package pkg);
    boolean deletePackage(int userId);
    boolean updatePackage(Package pkg);
    PageInfo<Package> queryPackage(PackageQueryDTO queryDTO);
} 