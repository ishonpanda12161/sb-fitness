package com.project.fitness.repository;

import com.project.fitness.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation,String> {

    List<Recommendation> findByUserId(String userId);

    List<Recommendation> findByActivityId(String activityId);

}
