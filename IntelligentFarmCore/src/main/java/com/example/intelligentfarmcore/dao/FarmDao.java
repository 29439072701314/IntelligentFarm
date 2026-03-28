package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmDao extends JpaRepository<Farm, Long> {
    // 根据用户ID查询农场
    List<Farm> findByUserId(Long userId);

    // 根据名称模糊查询
    @Query("SELECT f FROM Farm f WHERE f.farmName LIKE %:name%")
    List<Farm> findByFarmNameContaining(@Param("name") String name);

    // 根据地址模糊查询
    @Query("SELECT f FROM Farm f WHERE f.address LIKE %:address%")
    List<Farm> findByAddressContaining(@Param("address") String address);

    // 多条件组合查询
    @Query("SELECT f FROM Farm f WHERE (:farmName IS NULL OR f.farmName LIKE %:farmName%) AND (:address IS NULL OR f.address LIKE %:address%) AND (:livestockCount IS NULL OR SIZE(f.livestockList) = :livestockCount) AND (:deviceCount IS NULL OR SIZE(f.deviceList) = :deviceCount)")
    List<Farm> searchFarms(@Param("farmName") String farmName, 
                          @Param("address") String address, 
                          @Param("livestockCount") Integer livestockCount, 
                          @Param("deviceCount") Integer deviceCount);

    // 检查农场是否有关联的牲畜
    @Query("SELECT COUNT(l) FROM Livestock l WHERE l.farmId = :farmId")
    long countLivestockByFarmId(@Param("farmId") Long farmId);

    // 检查农场是否有关联的设备
    @Query("SELECT COUNT(d) FROM Device d WHERE d.farmId = :farmId")
    long countDevicesByFarmId(@Param("farmId") Long farmId);
}