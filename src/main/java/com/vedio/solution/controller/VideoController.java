package com.vedio.solution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vedio.solution.entity.Video;
import com.vedio.solution.service.VideoService;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

	@Autowired
	private VideoService videoService;

	@PostMapping("/upload")
	public ResponseEntity<Video> uploadVideo(@RequestParam("file") MultipartFile file,
			@RequestParam("resolution") String resolution // Accept resolution as a parameter
	) {
		Video videoTask = videoService.uploadVideo(file, resolution);
		return new ResponseEntity<>(videoTask, HttpStatus.CREATED);
	}
}