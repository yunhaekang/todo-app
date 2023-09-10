package com.yunoi.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
    // 다른 모델의 DTO 도 이 클래스를 이용해 리턴할 수 있도록 제네릭 사용.

    private String error;
    private List<T> data;
}
