package com.example.springsecurityexample.todo.dto;

import com.example.springsecurityexample.todo.TodoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {

    private Long todoId;
    private Integer projectId;
    private Long userId;
    private TodoType todoType;
    private String title;
    private String content;
    private boolean status;
}
