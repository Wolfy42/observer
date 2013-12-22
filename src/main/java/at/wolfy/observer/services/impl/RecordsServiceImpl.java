package at.wolfy.observer.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.wolfy.observer.services.RecordsService;

public class RecordsServiceImpl implements RecordsService {

	private static final String RECORDS_DIR = "/opt/observer/records";
	
	@Override
	public List<File> getRecords() {
		File dir = new File(RECORDS_DIR);
		List<File> fileNames = getFileNames(dir);		
		Collections.sort(fileNames, new Comparator<File>() {
			@Override
			public int compare(File f0, File f1) {
				return (-1)*f0.getName().compareTo(f1.getName());
			}
		});
		return fileNames;
	}
	
	private List<File> getFileNames(File dir) {
		List<File> fileNames = new ArrayList<>();
	    for (File entry : dir.listFiles()) {
	        if (entry.isDirectory()) {
	        	fileNames.addAll(getFileNames(entry));
	        } else if (entry.getName().endsWith(".wav")) {
	        	fileNames.add(entry);
	        }
	    }
	    return fileNames;
	}
}
