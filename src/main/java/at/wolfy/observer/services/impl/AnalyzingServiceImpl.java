package at.wolfy.observer.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.AnalyzingService;
import at.wolfy.observer.services.AudioRecordDAO;
import at.wolfy.observer.services.RecordsService;

public class AnalyzingServiceImpl implements AnalyzingService {

	private static Logger log = Logger.getLogger(AnalyzingServiceImpl.class);
	
	@Inject
	private RecordsService recordsService;
	
	@Inject
	private AudioRecordDAO audioRecordService;
	
	@Override
	public void analyzeRecords() {
		log.info("checking for records");
		List<File> records = recordsService.getRecords();
		List<Date> startDates = audioRecordService.findAllStartDates();
		log.info("Stored audioRecords: " + startDates.size());
		SimpleDateFormat recordFormatter = new SimpleDateFormat("yyyyMMddHHmm");
		
		for (File record : records) {
			try {
				String[] parts = record.getName().split("_");
				Date date = recordFormatter.parse(parts[0] + parts[1]);
				if (isNotAnalyzed(startDates, date)) {
					List<Integer> maxVolumes = calculateMaxVolumes(record);
					AudioRecord ar = new AudioRecord();
					ar.setStart(date);
					ar.setVolumes(maxVolumes);
					ar.setDirectory(record.getParentFile().getAbsolutePath());
					ar.setFile(record.getName());
					audioRecordService.add(ar);
				}
			} catch (IOException | ParseException | UnsupportedAudioFileException e) {
				log.error("Could not analyze file", e);
			}
		}
	}
	
	private boolean isNotAnalyzed(List<Date> startDates, Date startDate) {
		for (Date start : startDates) {
			if (start.getTime() == startDate.getTime()) {
				return false;
			}
		}
		return true;
	}

	private List<Integer> calculateMaxVolumes(File audioRecord) throws IOException, UnsupportedAudioFileException {
		log.info("Going to analyze " + audioRecord.getName());
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioRecord);
		AudioFormat audioFormat = audioInputStream.getFormat();
		int bytesPerFrame = audioFormat.getFrameSize();
		if (bytesPerFrame != 2) {
			throw new IllegalStateException("wrong file format");
		}
		int numBytes = ((int)audioFormat.getSampleRate()) * bytesPerFrame; // 1s
		byte[] audioBytes = new byte[numBytes];
		
		List<Integer> maxVolumes = new ArrayList<>();
		while ((audioInputStream.read(audioBytes)) != -1) {
			
			ByteBuffer bb = ByteBuffer.wrap(audioBytes);
			bb.order(ByteOrder.LITTLE_ENDIAN);
			int current;
			int[] values = new int[numBytes/2];
			int idx = 0;
			while (bb.hasRemaining()) {
				current = Math.abs(bb.getShort());
				values[idx] = current;
				idx++;
			}
			Arrays.sort(values); // slowest part in method
			int percent = values.length/ 100;
			int percentile = values[values.length-percent];
			maxVolumes.add(percentile);
		}
		return maxVolumes;
	}
}
