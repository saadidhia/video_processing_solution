package com.vedio.solution.Listner;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.vedio.solution.entity.Video;
import com.vedio.solution.service.VideoProcessingService;

@Component
public class VideoListener {

	private final VideoProcessingService videoProcessingService;

	public VideoListener(VideoProcessingService videoProcessingService) {
		this.videoProcessingService = videoProcessingService;
	}

	@RabbitListener(queues = "videoQueue")
	public void receiveMessage(Video video) {
		videoProcessingService.processVideo(video);
	}
}
