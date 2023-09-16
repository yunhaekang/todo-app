package com.yunoi.todo.api;

import org.junit.jupiter.api.Test;

import java.util.Base64;

public class JWTDecodeTest {

    @Test
    void jwtDecodingTest() {
        String encodedString = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmZjgwODA4MThhOWMyNjBlMDE4YTljMjY1M2NhMDAwMCIsImlhdCI6MTY5NDgzNjg3MiwiZXhwIjoxNjk0OTIzMjcyfQ.UEoWwv6lsPfb2UivZUwTUSUR24k9MxXlcmDrIyPDQanIZbaS3I21V1mq-gXCUHmkpG7N1_Gxu-ZTvpVWF5w7EQ";

        int startIdx = encodedString.indexOf(".");
        int endIdx = encodedString.lastIndexOf(".");

        String header = encodedString.substring(0, startIdx);
        String payload = encodedString.substring(startIdx+1, endIdx);

        header = new String(Base64.getDecoder().decode(header.getBytes()));
        payload = new String(Base64.getDecoder().decode(payload.getBytes()));

        System.out.println("header = " + header);
        System.out.println("payload = " + payload);
    }
}
