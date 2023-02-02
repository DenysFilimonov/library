package com.my.library.services;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadFile {

    /**
     * Load file from multipart form
     * @param   req HttpServletRequest contains <input type ="file" name="cover">
     * @throws ServletException while redirecting
     * @throws IOException while saving file to disk
     */
    public static String load(HttpServletRequest req) throws ServletException, IOException {
        Part filePart = req.getPart("cover");
        String filePath = filePart.getSubmittedFileName();
        Path p = Paths.get(filePath);
        String fileName = p.getFileName().toString();
        InputStream fileContent = filePart.getInputStream();
        File file = new File(req.getServletContext().getRealPath("/")+"/"+ConfigurationManager.getInstance().
                getProperty(ConfigurationManager.COVER_PATH) + fileName);
        copyInputStreamToFile(fileContent, file);
        return fileName;
    }


    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[1024];
            if(inputStream!=null)
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
            }
        }

    }

}