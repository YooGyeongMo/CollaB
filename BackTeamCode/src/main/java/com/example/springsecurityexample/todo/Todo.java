package com.example.springsecurityexample.todo;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "todoId")
@Entity
public class Todo {
    @Id
    @GeneratedValue
    private Long todoId;
    private Integer projectId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private TodoType todoType = TodoType.PERSONAL;
    private String title;
    private String content;
    private boolean status;
}
