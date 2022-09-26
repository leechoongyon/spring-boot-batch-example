package com.example.batch.domain.flatFile;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

public class FlatFileExampleInfo {

    @Data
    public static class Base {
        private String id;  // size 10
        private String name;    // size 10
        private int age;    // size 3

        @Builder
        public Base(String id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public static String getFormat() {
            StringBuffer sb = new StringBuffer(); // thread-safe
            sb.append("%-10s"); // id 왼쪽 정렬이며, 오른쪽에 값 없으면 space
            sb.append("%-10s"); // name 왼쪽 정렬이며, 오른쪽에 값 없으면 space
            sb.append("%03d");  // age 오른쪽정렬이며, 왼쪽에 값이 없으면 0 으로 채움.
            return sb.toString();
        }

        public static String[] getNames() {
            return new String[] {
                "id", "name", "age"
            };
        }
    }
}
