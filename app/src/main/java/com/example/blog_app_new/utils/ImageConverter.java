package com.example.blog_app_new.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageConverter {
    public static List<String> convertUrisToBase64(List<Uri> uris, ContentResolver contentResolver) {
        List<String> base64Strings = new ArrayList<>();

        for (Uri uri : uris) {
            try {
                InputStream inputStream = contentResolver.openInputStream(uri);
                if (inputStream != null) {
                    byte[] imageBytes = readBytes(inputStream);
                    String base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    base64Strings.add(base64String);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return base64Strings;
    }

    private static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}
