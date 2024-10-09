package com.vedio.solution.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.vedio.solution.entity.Video;
import com.vedio.solution.repository.VideoRepository;

import jakarta.transaction.Transactional;

@Service
public class VideoProcessingService {
	private boolean is720pProcessing = false;
	@Autowired
	private VideoRepository videoRepository;

	@Autowired
	private ResourceLoader resourceLoader;

	private String getFfmpegPath() {
		try {
			return resourceLoader.getResource("classpath:ffmpeg.exe").getFile().getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException("Could not load ffmpeg executable", e);
		}
	}

	@Transactional
	public synchronized void processVideo(Video video) {


		if ("480p".equals(video.getResolution())) {
			process480p(video);
		} else if ("720p".equals(video.getResolution())) {
			while (is720pProcessing) {
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			process720p(video);
		}

		videoRepository.save(video);
	}

	private void process480p(Video video) {
		video.setStatus("PROCESSING");
		String command = String.format("ffmpeg -i %s -vf scale=854:480 %s_480p.mp4", video.getVideoPath(),
				video.getVideoPath());
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			video.setStatus("COMPLETED");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			video.setStatus("FAILED");
		}
	}

	private void process720p(Video video) {
		is720pProcessing = true;
		video.setStatus("PROCESSING");

		String command = String.format("ffmpeg -i %s -vf scale=1280:720 %s_720p.mp4", video.getVideoPath(),
				video.getVideoPath());
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			video.setStatus("COMPLETED");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			video.setStatus("FAILED");
		} finally {
			is720pProcessing = false;
			notifyAll();
		}
	}
}
