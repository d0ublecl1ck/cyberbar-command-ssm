package org.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.entity.Package;
import org.example.dto.PackageQueryDTO;
import org.example.mapper.PackageMapper;
import org.example.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageMapper packageMapper;

    @Override
    public boolean addPackage(Package pkg) {
        return packageMapper.insertPackage(pkg) > 0;
    }

    @Override
    public boolean deletePackage(int userId) {
        return packageMapper.deletePackage(userId) > 0;
    }

    @Override
    public boolean updatePackage(Package pkg) {
        return packageMapper.updatePackage(pkg) > 0;
    }

    @Override
    public PageInfo<Package> queryPackage(PackageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<Package> packages = packageMapper.selectPackage(queryDTO);
        return new PageInfo<>(packages);
    }
} 