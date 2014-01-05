package at.wolfy.observer.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.sessionstate.AudioRecords;
import at.wolfy.observer.sessionstate.AudioRecords.AudioRecordDay;
import at.wolfy.observer.sessionstate.AudioRecords.AudioRecordHour;
import at.wolfy.observer.streams.AttachmentStreamResponse;
import at.wolfy.observer.streams.AttachmentStreamResponse.ContentType;

public class RecordsList {

	private SimpleDateFormat dateFormatterDay = new SimpleDateFormat("dd.MM.yyyy");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	@SessionState
	private AudioRecords audioRecords;
	
	@Property
	private AudioRecordDay audioRecordDay;
	
	@Property
	private AudioRecordHour audioRecordHour;
	
	@Property 
	private AudioRecord audioRecord;
	
	public boolean isShowAudioRecordDays() {
		return audioRecords.getSelectedDay() == null;
	}
	
	public boolean isShowAudioRecordHours() {
		return !isShowAudioRecordDays() && audioRecords.getSelectedHour() == null;
	}
	
	public boolean isShowAudioRecords() {
		return !isShowAudioRecordDays() && !isShowAudioRecordHours();
	}
	
	public List<AudioRecordDay> getAudioRecordDays() {
		return audioRecords.getAudioRecordDays();
	}
	
	public List<AudioRecordHour> getAudioRecordHours() {
		if (audioRecords.getSelectedDay() == null) {
			return Collections.emptyList();
		}
		return audioRecords.getSelectedDay().getAudioRecordHours();
	}
	
	public List<AudioRecord> getAudioRecords() {
		if (audioRecords.getSelectedHour() == null) {
			return Collections.emptyList();
		}
		return audioRecords.getSelectedHour().getAudioRecords();
	}
	
	public String getAudioRecordDayName() throws ParseException {
		return dateFormatterDay.format(audioRecordDay.getStart());
	}
	
	public String getAudioRecordHourName() throws ParseException {
		return dateFormatter.format(audioRecordHour.getStart());
	}
	
	public String getAudioRecordName() throws ParseException {
		return dateFormatter.format(audioRecord.getStart());
	}
	
	public int getAudioRecordDayIndex() {
		return audioRecords.getAudioRecordDays().indexOf(audioRecordDay);
	}
	
	public int getAudioRecordHourIndex() {
		return audioRecords.getSelectedDay().getAudioRecordHours().indexOf(audioRecordHour);
	}
	
	public int getAudioRecordIndex() {
		return audioRecords.getSelectedHour().getAudioRecords().indexOf(audioRecord);
	}
	
	public List<AudioRecord> getAudioRecordDayRecords() {
		List<AudioRecord> ars = new LinkedList<>();
		Integer maxVolume;
		for (AudioRecordHour arh: audioRecordDay.getAudioRecordHours()) {
			maxVolume = 0;
			for (AudioRecord ar : arh.getAudioRecords()) {
				for (Integer vol : ar.getVolumes()) {
					maxVolume = Math.max(0, vol);
				}
				AudioRecord reducedAr = new AudioRecord();
				reducedAr.setStart(ar.getStart());
				reducedAr.setVolumes(asList(maxVolume));
				ars.add(reducedAr);
			}
		}
		return ars;
	}
	
	private List<Integer> asList(Integer value) {
		return Arrays.asList(new Integer[]{value});
	}
	
	public List<AudioRecord> getAudioRecordHourRecords() {
		return audioRecordHour.getAudioRecords();
	}
	
	@OnEvent(component="audioRecordDaySelectLink")
	public void selectDay(int index) {
		AudioRecordDay selected = audioRecords.getAudioRecordDays().get(index);
		audioRecords.setSelectedDay(selected);
	}
	
	@OnEvent(component="audioRecordHourSelectLink")
	public void selectHour(int index) {
		AudioRecordHour selected =  audioRecords.getSelectedDay().getAudioRecordHours().get(index);
		audioRecords.setSelectedHour(selected);
	}
	
	@OnEvent(component="audioRecordSelectLink")
	public StreamResponse getRecordStream(int audioRecordIndex) throws FileNotFoundException {
		AudioRecord selected = audioRecords.getSelectedHour().getAudioRecords().get(audioRecordIndex);
		String path = selected.getDirectory() + File.separator + selected.getFile();
		InputStream input = new FileInputStream(path);
		return new AttachmentStreamResponse(input, selected.getFile(), ContentType.WAV);
	}
	
	@OnEvent(component="updateRecordsLink")
	public void updateRecords() {
		audioRecords.refresh();
	}
	
	@OnEvent(component="deselectDayLink")
	public void deselectDayLink() {
		audioRecords.setSelectedDay(null);
	}
	
	@OnEvent(component="deselectHourLink")
	public void deselectHourLink() {
		audioRecords.setSelectedHour(null);
	}
}
