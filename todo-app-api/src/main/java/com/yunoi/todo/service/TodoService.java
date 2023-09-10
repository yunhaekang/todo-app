package com.yunoi.todo.service;

import com.yunoi.todo.model.TodoEntity;
import com.yunoi.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    // 생성
    public List<TodoEntity> create(final TodoEntity entity) {
        // validations
        validate(entity);

        repository.save(entity);

        log.info("Entity Id: {} is saved", entity.getId());

        return repository.findByUserID(entity.getUserId());
    }

    // 검색
    public List<TodoEntity> retrieve(final String userId) {
        // 유저 아이디로 검색
        log.info("userId param: {}", userId);

        List<TodoEntity> list = new ArrayList<>();
        list = repository.findByUserID(userId);

        log.info("found todo list: {}", list.toString());

        return list;
    }

    // 수정
    public List<TodoEntity> update(final TodoEntity entity) {
        // 저장할 엔티티가 유효한지 검증
        validate(entity);

        // 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. (존재하지 않는 엔티티는 업데이트 할 수 없기 때문)
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            // 반환된 todoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // 데이터베이스에 새 값을 저장한다.
            repository.save(entity);
        });

        // 해당 유저의 모든 투두 리스트를 리턴한다.
        return retrieve(entity.getUserId());
    }

    // 삭제
    public List<TodoEntity> delete(final TodoEntity entity) {
        // 저장할 엔티티가 유효한지 검증
        validate(entity);

        try {
            // 엔티티를 삭제한다.
            repository.delete(entity);
        } catch (Exception e) {
            // 예외 발생 시 id와 exception을 로깅한다.
            log.error("error deleting entity ", entity.getId(), e);

            // 컨트롤러로 exception 을 날린다. 데이터베이스 내부 로직 캡슐화를 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
            throw new RuntimeException("error deleteing entity " + entity.getId());
        }

        // 새 투두 리스트를 가져와 리턴한다.
        return retrieve(entity.getUserId());
    }

    // 검증을 위해 분리한 메서드
    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    // 테스트 메서드
    public String testService() {
        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        // TodoEntity 저장
        repository.save(entity);
        // TodoEntity 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }
}
