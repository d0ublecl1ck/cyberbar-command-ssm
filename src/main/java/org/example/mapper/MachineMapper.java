package org.example.mapper;

import org.example.entity.Machine;
import org.example.dto.MachineQueryDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.math.BigDecimal;

public interface MachineMapper {
    List<Machine> selectAll();
    Machine selectById(Integer id);
    List<Machine> selectByZoneId(Integer zoneId);
    int insert(Machine machine);
    int update(Machine machine);
    int deleteById(Integer id);
    int countTotal();
    int countByStatus(@Param("status") String status);
    List<Machine> selectByCondition(MachineQueryDTO queryDTO);
    int getTotalMachinesCountByZone(@Param("zoneId") Integer zoneId);
    int getCountByStatusAndZone(@Param("status") String status, @Param("zoneId") Integer zoneId);
    BigDecimal getAveragePrice();
    BigDecimal getMaxPrice();
    BigDecimal getMinPrice();
    int updateZoneIdToNull(@Param("zoneId") Integer zoneId);
    List<Machine> selectByNullZoneId(MachineQueryDTO queryDTO);
} 