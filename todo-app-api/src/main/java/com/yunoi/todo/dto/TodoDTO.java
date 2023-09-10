package com.yunoi.todo.dto;

import com.yunoi.todo.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
    /**
     * 서버에서 요청을 처리하고 클라이언트로 반환할 때
     * 모델 자체를 그대로 리턴하는 경우는 별로 없다. (게다가 추천하지 않는 방법)
     * 데이터 전달을 위해서 따로 사용하는 오브젝트인 DTO(Data Transfer Object)로 변환해서 리턴한다.
     * 1. 비즈니스 로직을 캡슐화하기 위해
     *      모델은 데이터베이스 테이블 구조와 매우 유사하므로 모델이 가지고 있는 필드들이
     *      테이블의 스키마와 비슷할 확률이 높다. 외부 사용자에게 서비스 내부의 로직, 데이터베이스 구조 등을 숨기기 위해
     * 2. 클라이언트가 필요한 정보를 모델이 전부 포함하지 않는 경우
     *      예를 들어 에러 메시지. 서비스 로직과 관계 없으므로 모델에 담기는 애매하다. 이런 것들을 DTO 에 필드로 선언하여 사용하면 된다.
     */

    private String id;
    private String title;
    private boolean done;

    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    public static TodoEntity toEntity(final TodoDTO dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }
}
