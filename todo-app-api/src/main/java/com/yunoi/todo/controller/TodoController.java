package com.yunoi.todo.controller;

import com.yunoi.todo.dto.ResponseDTO;
import com.yunoi.todo.dto.TodoDTO;
import com.yunoi.todo.model.TodoEntity;
import com.yunoi.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto){
        try{
            // TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            // id를 null로 초기화한다. 생성 당시에는 id가 없어야 하기 때문이다.
            entity.setId(null);

            /* 인증 추가 이전 코드
            // 임시 유저 아이디를 설정해 준다.
            // String tempUserId = "temp-user";
            // entity.setUserId(tempUserId);
            */

            // Authentication Bearer Token 을 통해 받은 userId를 넘긴다.
            entity.setUserId(userId);

            // 서비스를 호출하여 투두 엔티티를 생성한다.
            List<TodoEntity> entities = service.create(entity);

            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 예외 발생시 dto 대신 error에 메시지를 넣어서 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
        //String tempUserId = "temp-user"; // temporary user id

        // 서비스의 retrieve 메서드를 불러 투두 리스트를 가져온다.
        List<TodoEntity> entities = service.retrieve(userId);

        // 자바 스트림을 이용하여 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 변환된 dto 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto) {

        // dto를 entity 로 변환한다.
        TodoEntity entity = TodoDTO.toEntity(dto);

        /*
        // userId를 tempUserId로 초기화한다. (수정 예정)
        entity.setUserId(tempUserId);
         */
        // Authentication Bearer Token 을 통해 받은 userId를 넘긴다
        entity.setUserId(userId);

        // 서비스 호출하여 entity를 업데이트한다.
        List<TodoEntity> entities = service.update(entity);

        // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 변환된 dto 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto) {
        try {
            // TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            /*
            // 임시 유저 아이디를 설정해준다. (추후 변경할 것)
            String tempUserId = "temp-user"; // temporary user id
            entity.setUserId(tempUserId); */

            // Authentication Bearer Token 을 통해 받은 userId를 넘긴다
            entity.setUserId(userId);

            // 서비스를 호출하여 entity를 삭제한다.
            List<TodoEntity> entities = service.delete(entity);

            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 예외가 발생할 경우 dto 대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    //////////////////////////////////////////////////////////////////
    // 테스트 메서드: 스스로 ResponseEntity를 리턴하는 HTTP GET testTodo()
    @GetMapping("test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService(); // 테스트 서비스 사용
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }
}
