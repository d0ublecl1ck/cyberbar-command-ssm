package org.example.service;

import com.github.pagehelper.PageInfo;
import org.example.dto.PackageQueryDTO;
import org.example.entity.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-mvc.xml"})
@Transactional
public class PackageServiceTest {

    @Autowired
    private PackageService packageService;

    private Random random = new Random();

    private int generateRandomUserId() {
        return 10000 + random.nextInt(90000); // 生成10000到99999之间的随机数
    }

    @Test
    public void testAddPackage() {
        Package pkg = new Package();
        pkg.setUserId(generateRandomUserId());
        pkg.setDiscount(new BigDecimal("0.85"));
        pkg.setStartTime(LocalDateTime.now());
        pkg.setEndTime(LocalDateTime.now().plusDays(30));
        pkg.setAdminId(1);

        assertTrue(packageService.addPackage(pkg));
    }

    @Test
    public void testDeletePackage() {
        // 先添加一个包
        Package pkg = new Package();
        int userId = generateRandomUserId();
        pkg.setUserId(userId);
        pkg.setDiscount(new BigDecimal("0.85"));
        pkg.setStartTime(LocalDateTime.now());
        pkg.setEndTime(LocalDateTime.now().plusDays(30));
        pkg.setAdminId(1);
        packageService.addPackage(pkg);

        assertTrue(packageService.deletePackage(userId));
    }

    @Test
    public void testUpdatePackage() {
        // 先添加一个包
        Package pkg = new Package();
        int userId = generateRandomUserId();
        pkg.setUserId(userId);
        pkg.setDiscount(new BigDecimal("0.85"));
        pkg.setStartTime(LocalDateTime.now());
        pkg.setEndTime(LocalDateTime.now().plusDays(30));
        pkg.setAdminId(1);
        packageService.addPackage(pkg);

        // 更新包
        Package updatePkg = new Package();
        updatePkg.setUserId(userId);
        updatePkg.setDiscount(new BigDecimal("0.75"));
        assertTrue(packageService.updatePackage(updatePkg));

        // 验证更新结果
        PackageQueryDTO queryDTO = new PackageQueryDTO();
        queryDTO.setUserId(userId);
        PageInfo<Package> pageInfo = packageService.queryPackage(queryDTO);
        assertEquals(new BigDecimal("0.75"), pageInfo.getList().get(0).getDiscount());
    }

    @Test
    public void testQueryPackage() {
        // 先添加一个包
        Package pkg = new Package();
        int userId = generateRandomUserId();
        pkg.setUserId(userId);
        pkg.setDiscount(new BigDecimal("0.85"));
        pkg.setStartTime(LocalDateTime.now());
        pkg.setEndTime(LocalDateTime.now().plusDays(30));
        pkg.setAdminId(1);
        packageService.addPackage(pkg);

        // 测试各种查询条件
        PackageQueryDTO queryDTO = new PackageQueryDTO();
        
        // 按用户ID查询
        queryDTO.setUserId(userId);
        PageInfo<Package> pageInfo = packageService.queryPackage(queryDTO);
        assertNotNull(pageInfo);
        assertTrue(pageInfo.getList().size() > 0);
        assertEquals(userId, pageInfo.getList().get(0).getUserId().intValue());

        // 按折扣查询
        queryDTO = new PackageQueryDTO();
        queryDTO.setDiscount(new BigDecimal("0.85"));
        pageInfo = packageService.queryPackage(queryDTO);
        assertTrue(pageInfo.getList().size() > 0);
        assertEquals(new BigDecimal("0.85"), pageInfo.getList().get(0).getDiscount());

        // 测试分页
        queryDTO = new PackageQueryDTO();
        queryDTO.setPageSize(5);
        queryDTO.setPageNum(1);
        pageInfo = packageService.queryPackage(queryDTO);
        assertNotNull(pageInfo);
        assertTrue(pageInfo.getPageSize() == 5);
    }
} 