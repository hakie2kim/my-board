package com.pf.www.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {
	private final String SAVE_PATH;
	
	private FileUtil() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		SAVE_PATH = "C:/dev/tmp/" + year + "/" + month + "/" + day;
	}

	public File saveFile(MultipartFile mf) throws IllegalStateException, IOException {
		File file = new File(SAVE_PATH);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		
		file = new File(SAVE_PATH, UUID.randomUUID().toString().replaceAll("-", ""));
		mf.transferTo(file);
		
		return file;
	}
}
