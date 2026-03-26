package com.example.intelligentfarmcore.dao;

import com.example.intelligentfarmcore.pojo.entity.DiseaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRecordDao extends JpaRepository<DiseaseRecord, Long> {
    // 根据牲畜编号模糊查询
    List<DiseaseRecord> findByLivestockCodeContainingAndDeletedFalse(String livestockCode);

    // 根据疾病名称模糊查询
    List<DiseaseRecord> findByDiseaseNameContainingAndDeletedFalse(String diseaseName);

    // 根据状态查询
    List<DiseaseRecord> findByStatusAndDeletedFalse(String status);

    // 统计总数
    long countByDeletedFalse();

    // 统计治疗中数量
    long countByStatusAndDeletedFalse(String status);
    
    // 根据多个牲畜编号查询
    List<DiseaseRecord> findByLivestockCodeIn(List<String> livestockCodes);
}
