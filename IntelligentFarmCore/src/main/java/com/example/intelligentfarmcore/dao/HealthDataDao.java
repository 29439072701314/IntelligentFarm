package com.example.intelligentfarmcore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.intelligentfarmcore.pojo.entity.HealthData;

public interface HealthDataDao extends JpaRepository<HealthData, Long> {

}
