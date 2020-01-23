package com.example.demo.domain.repository;

import com.example.demo.domain.model.entity.Campaign;
import com.example.demo.domain.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByCategory_TitleIn(List<String> categoryList);
}