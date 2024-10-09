package com.vedio.solution.service;

import java.io.File;
import java.io.IOException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vedio.solution.entity.Video;

@Service
public class VideoService {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public Video uploadVideo(MultipartFile file, String resolution) {
		try {
			File tempFile = File.createTempFile("video-", file.getOriginalFilename());
			file.transferTo(tempFile);

			Video video = new Video();
			video.setVideoPath(tempFile.getAbsolutePath());
			video.setStatus("PENDING");
			video.setResolution(resolution);

			rabbitTemplate.convertAndSend("videoQueue", video);

			return video;
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload video", e);
		}
	}
}