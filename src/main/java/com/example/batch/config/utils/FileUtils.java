package com.example.batch.config.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class FileUtils {

    /**
     * 파일 라인을 리턴합니다.
     * @return
     */
    public static int getFileLineCount(File file) {
        try {
            return Optional.ofNullable(org.apache.commons.io.FileUtils.readLines(file))
                    .orElse(new ArrayList()).size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
