package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.Warning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WarningDao extends JpaRepository<Warning, Long> {
    Page<Warning> findByTypeAndStatus(String type, String status, Pageable pageable);
    Page<Warning> findByType(String type, Pageable pageable);
    Page<Warning> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT COUNT(w) FROM Warning w")
    long countTotal();
    
    @Query("SELECT COUNT(w) FROM Warning w WHERE w.status = '未处理'")
    long countUnprocessed();
    
    @Query("SELECT COUNT(w) FROM Warning w WHERE w.type = '牲畜' AND w.status = '未处理'")
    long countLivestockAbnormal();
    
    @Query("SELECT COUNT(w) FROM Warning w WHERE w.type = '环境' AND w.status = '未处理'")
    long countEnvironmentAbnormal();
    
    @Query("SELECT COUNT(w) FROM Warning w WHERE w.type = :type AND w.source = :source AND w.status = '未处理'")
    long countByTypeAndSourceAndStatus(@Param("type") String type, @Param("source") String source);
}