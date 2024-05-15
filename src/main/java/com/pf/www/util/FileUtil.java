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
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		SAVE_PATH = "C:/dev/tmp/" + year + "/" + month + "/" + day;
	}

	public File saveFile(MultipartFile mf) throws IllegalStateException, IOException {
		File file = new File(SAVE_PATH);
		
		// 1. SAVE_PATH 폴더가 없는 경우 만든다.
		if (!file.exists()) {
			file.mkdirs();
		}
		
		// 2. 파일 이름을 UUID를 이용해 바꾼다.
		file = new File(SAVE_PATH, UUID.randomUUID().toString().replaceAll("-", ""));
		mf.transferTo(file);
		
		return file;
	}
}
