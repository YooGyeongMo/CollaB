package com.example.springsecurityexample.todo.repository;

import com.example.springsecurityexample.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserId(Long userId);
    Todo findByTodoId(Long todoId);
    Todo deleteVoteByTodoId(Long todoId);
}
