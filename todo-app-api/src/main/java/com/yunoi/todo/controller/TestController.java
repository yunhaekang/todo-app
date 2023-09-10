package com.yunoi.todo.controller;

import com.yunoi.todo.dto.ResponseDTO;
import com.yunoi.todo.dto.TestRequestBodyDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public String testController() {
        return "Hello World!";
    }

    @GetMapping("getMappingTest")
    public String testControllerWithPath() {
        return "Hello World! getMappingTest ";
    }

    @GetMapping("/{id}")
    public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
        return "Hello World! ID: " + id;
    }

    @GetMapping("requestParamTest")
    public String testControllerRequestParam(@RequestParam(required = false) int id) {
        return "Hello World! ID: " + id;
    }

    @GetMapping("requestBodyTest")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello World! ID: " + testRequestBodyDTO.getId() + "\n"
                + "Message: " + testRequestBodyDTO.getMessage();
    }

    @GetMapping("responseBodyTest")
    public ResponseDTO<String> testControllerResponseBody() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! this is a ResponseDTO");
        ResponseDTO<String> response = ResponseDTO.<String>builder()
                .data(list)
                .build();
        return response;
    }

    @GetMapping("responseEntityTest")
    public ResponseEntity<?> testControllerResponseEntity() {
        /*  ResponseEntity 는 HTTP 응답의 바디뿐만이 아니라 여러 다른 매개변수들,
        *   예를 들어 status나 header를 조작하고 싶을 떄 사용한다.
        */
        List<String> list = new ArrayList<>();
        list.add("Hello World! this is a ResponseEntity. And you got 400!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        // http status를 400으로 설정.
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("okResponseTest")
    public ResponseEntity<?> okResponseTest() {
        List<String> list = new ArrayList<>();
        list.add("It's OK!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    /**
     *  jwt 헤더와 페이로드 디코딩하여 확인
     * */
    @PostMapping("decodeBase64")
    public String decodeBase64(@RequestBody String encodedString) {

        int startIdx = encodedString.indexOf(".");
        int endIdx = encodedString.lastIndexOf(".");

        String header = encodedString.substring(0, startIdx);
        String payload = encodedString.substring(startIdx+1, endIdx);

        header = new String(Base64.getDecoder().decode(header.getBytes()));
        payload = new String(Base64.getDecoder().decode(payload.getBytes()));

        System.out.println("header = " + header);
        System.out.println("payload = " + payload);

        return header + payload;
    }
}
