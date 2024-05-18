package com.pf.www.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.pf.www.forum.notice.dto.BoardAttachDto;

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
		int extIdx = mf.getOriginalFilename().indexOf('.');

		file = new File(SAVE_PATH,
				UUID.randomUUID().toString().replaceAll("-", "") + mf.getOriginalFilename().substring(extIdx));
		mf.transferTo(file);

		return file;
	}

    public File createZipFile(List<BoardAttachDto> filesInfo) throws IOException {
        List<File> files = new ArrayList<>();

        // 파일 객체 생성 및 리스트에 추가
        for (BoardAttachDto fileInfo : filesInfo) {
        	String renamedFileSavePath = fileInfo.getSavePath();
            File renamedFile = new File(renamedFileSavePath);
            File orgFile = new File(renamedFileSavePath.substring(0, renamedFileSavePath.lastIndexOf("\\") + 1) + fileInfo.getOrgFileNm());
            FileUtils.copyFile(renamedFile, orgFile);
            files.add(orgFile);
        }

        // 임시 파일로 생성된 ZIP 파일을 저장할 위치와 이름 설정)
        File zipFile = File.createTempFile("compressed_files", ".zip");

        // ZIP 파일 생성
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            // 각 파일을 ZIP 파일에 추가
            for (File file : files) {
                // 파일이 존재하고 파일일 경우에만 압축 진행
                if (file.exists() && file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        // ZIP 엔트리 생성
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);

                        // 파일 데이터를 버퍼를 사용하여 읽고, ZIP 파일에 작성
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) >= 0) {
                            zos.write(buffer, 0, length);
                        }
                    }
                } else {
                    System.err.println("파일을 찾을 수 없습니다: " + file.getAbsolutePath());
                }
            }
        }
        
        // 원래 이름으로 복원한 파일들 삭제
        for (File orgFile : files) {
        	orgFile.delete();
        }

        // 생성된 ZIP 파일을 리턴
        return zipFile;
    }
}
