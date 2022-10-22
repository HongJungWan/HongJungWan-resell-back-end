package com.resell.resell.common.utils.file;

import com.resell.resell.exception.file.IllegalMimeTypeException;

import java.util.UUID;

public class FileNameUtils {

    public static void checkImageMimeType(String mimeType) {
        if (!(mimeType.equals("image/jpg") || mimeType.equals("image/jpeg")
                || mimeType.equals("image/png") || mimeType.equals("image/gif"))) {
            throw new IllegalMimeTypeException();
        }
    }

    public static String fileNameConvert(String fileName) {
        StringBuilder builder = new StringBuilder();
        UUID uuid = UUID.randomUUID();
        String extension = getExtension(fileName);

        builder.append(uuid).append(".").append(extension);

        return builder.toString();
    }

    private static String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");

        return fileName.substring(pos + 1);
    }

    public static String getFileName(String path) {
        int idx = path.lastIndexOf("/");

        return path.substring(idx + 1);
    }

    // origin -> thumbnail 에서 잠깐 origin -> origin으로 변경, 일단 이렇게 변경 후 잘 동작, 좀 더 고민해보기.
    public static String toThumbnail(String src) {
        return src.replaceFirst("origin", "origin");
    }

    public static String toResized(String src) {
        return src.replaceFirst("origin", "resized");
    }

}
