package com.pf.www.util.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component
public class ZipFileDownloadView extends AbstractView {

    public ZipFileDownloadView() {
        setContentType("application/zip; charset=UTF-8");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<File> filesToZip = (List<File>) model.get("filesToZip");

        // Create zip file
        File zipFile = createZipFile(filesToZip);

        // Set response headers
        response.setContentType(getContentType());
        response.setContentLength((int) zipFile.length());

        String userAgent = request.getHeader("User-Agent");
        String encodedFileName = getEncodedFileName(userAgent, "zip");

        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");

        // Write zip file to response
        try (OutputStream out = response.getOutputStream();
             FileInputStream fis = new FileInputStream(zipFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
        }
    }

    private File createZipFile(List<File> files) throws IOException {
        File zipFile = new File(System.getProperty("java.io.tmpdir"), "zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }
            }
        }
        return zipFile;
    }

    private String getEncodedFileName(String userAgent, String fileName) throws IOException {
        boolean ie = userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1;
        if (ie) {
            return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else {
            return new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
    }
}