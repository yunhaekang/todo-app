package com.yunoi.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    /**
     * AWS 로드 밸런서는 기본 경로인 "/"에 HTTP 요청을 보내 애플리케이션이 동작하는지 확인한다.
     * 일래스틱 빈스톡은 이를 기반으로 애플리케이션이 실행 중인 상태인지, 주의가 필요한 상태인지 확인해준다.
     * 또 이 상태를 AWS 콘솔 화면에 표시 해준다. 이를 위해 "/"에 간단한 API를 생성해 준다.
     */
    @GetMapping("/")
    public String healthCheck() {
        return "The service is up and running...";
    }
}

