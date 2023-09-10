package com.yunoi.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    private String id;

    @Column(nullable = false)
    private String username; // 아이디로 사용할 유저명. 이메일 또는 그냥 문자열

    private String password;    // 패스워드가 null 이면 안되지 않을까? 그러나 OAuth를 이용해 SSO를 구현할 것이므로 null을 허용했다.
    private String role;    // 사용자 권한 ex. 어드민, 일반 사용자
    private String authProvider; // 이후 OAuth에서 사용할 유저 정보 제공자: github
}
