package com.yunoi.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Todo")
public class TodoEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;      // 이 오브젝트의 아이디
    private String userId;  // 이 오브젝트를 생성한 유저의 아이디
    private String title;   // 투두 타이틀 ex. 운동하기
    private boolean done;   // 투두를 완료한 경우 (checked) true
}
