package com.example.batch.domain.classifierItemWriter;

import lombok.Builder;
import lombok.Getter;

public class ClassifierItemWriterExampleInfo {
    @Getter
    public static class Base {
        private String id;
        private String name;
        private int age;

        @Builder
        public Base(String id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
    }

    @Getter
    public static class MultiDto {
        private Header header;
        private Data data;
        private Tailer tailer;
    }
    @Getter
    public static class Header {
        private String headerContent;
    }
    @Getter
    public static class Data {
        private String dataContent;
    }
    @Getter
    public static class Tailer {
        private String tailerContent;
    }
}
