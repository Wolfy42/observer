package at.wolfy.observer.entities;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AudioRecord.class)
public class AudioRecord_ {

	public static volatile SingularAttribute<AudioRecord, Date> start;
	public static volatile SingularAttribute<AudioRecord, int[]> volumes;
	public static volatile SingularAttribute<AudioRecord, String> directory;
	public static volatile SingularAttribute<AudioRecord, String> file;
}
