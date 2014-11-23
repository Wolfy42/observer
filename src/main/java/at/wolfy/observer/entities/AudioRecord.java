package at.wolfy.observer.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AudioRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Date start;
	
	private int[] volumes;
	
	private String directory;
	private String file;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public int[] getVolumes() {
		return volumes;
	}

	public void setVolumes(int[] volumes) {
		this.volumes = volumes;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
