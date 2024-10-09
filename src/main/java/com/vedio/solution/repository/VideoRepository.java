package com.vedio.solution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vedio.solution.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

}