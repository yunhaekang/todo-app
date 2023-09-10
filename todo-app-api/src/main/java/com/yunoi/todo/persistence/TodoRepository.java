package com.yunoi.todo.persistence;

import com.yunoi.todo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    // JpaRepository<T, ID> : T는 테이블에 매핑할 엔티티 클래스, ID는 이 엔티티의 기본 키 자료형.

    @Query(value = "select t from TodoEntity t where t.userId = ?1")
    List<TodoEntity> findByUserID(String userId);
}
