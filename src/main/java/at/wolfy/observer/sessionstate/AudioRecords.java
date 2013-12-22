package at.wolfy.observer.sessionstate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.AudioRecordDAO;

public class AudioRecords {
	
	private AudioRecordDAO audioRecordDAO;
	private List<AudioRecordDay> audioRecordDays;
	private AudioRecordDay selectedDay;
	private AudioRecordHour selectedHour;
	
	public AudioRecords(AudioRecordDAO audioRecordDAO) {
		this.audioRecordDAO = audioRecordDAO;
		this.audioRecordDays = new LinkedList<>();
		init();
	}
	
	public List<AudioRecordDay> getAudioRecordDays() {
		return audioRecordDays;
	}
	
	public void refresh() {
		init();
	}
	
	public AudioRecordDay getSelectedDay() {
		return selectedDay;
	}
	
	public void setSelectedDay(AudioRecordDay selectedDay) {
		this.selectedDay = selectedDay;
	}
	
	public AudioRecordHour getSelectedHour() {
		return selectedHour;
	}
	
	public void setSelectedHour(AudioRecordHour selectedHour) {
		this.selectedHour = selectedHour;
	}
	
	private void init() {
		selectedDay = null;
		selectedHour = null;
		List<AudioRecord> audioRecords = audioRecordDAO.findAll();
		audioRecordDays.clear();
		Calendar cal = GregorianCalendar.getInstance();
		for (AudioRecord ar : audioRecords) {
			cal.setTime(ar.getStart());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			AudioRecordDay audioRecordDay = getAudioRecordDay(cal.getTime());
			cal.setTime(ar.getStart());
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			AudioRecordHour audioRecordHour = getAudioRecordHour(cal.getTime(), audioRecordDay);
			audioRecordHour.getAudioRecords().add(ar);
		}
	}

	private AudioRecordDay getAudioRecordDay(Date start) {
		for (AudioRecordDay ard : audioRecordDays) {
			if (ard.getStart().equals(start)) {
				return ard; 
			}
		}
		AudioRecordDay ard = new AudioRecordDay(start);
		audioRecordDays.add(ard);
		return ard;
	}
	
	
	private AudioRecordHour getAudioRecordHour(Date start, AudioRecordDay audioRecordDay) {
		for (AudioRecordHour arh : audioRecordDay.getAudioRecordHours()) {
			if (arh.getStart().equals(start)) {
				return arh;
			}
		}
		AudioRecordHour arh = new AudioRecordHour(start);
		audioRecordDay.getAudioRecordHours().add(arh);
		return arh;
	}

	public class AudioRecordDay {
		private Date start;
		private List<AudioRecordHour> audioRecordHours;
		public AudioRecordDay(Date start) {
			this.start = start;
			audioRecordHours = new LinkedList<>();
		}
		public Date getStart() {
			return start;
		}
		public List<AudioRecordHour> getAudioRecordHours() {
			return audioRecordHours;
		}
	}
	
	public class AudioRecordHour {
		private Date start;
		private List<AudioRecord> audioRecords;
		public AudioRecordHour(Date start) {
			this.start = start;
			audioRecords = new LinkedList<>();
		}
		public Date getStart() {
			return start;
		}
		public List<AudioRecord> getAudioRecords() {
			return audioRecords;
		}
	}
}
