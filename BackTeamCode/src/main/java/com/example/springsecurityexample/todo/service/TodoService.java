package com.example.springsecurityexample.todo.service;

import com.example.springsecurityexample.match.Error;
import com.example.springsecurityexample.match.Match;
import com.example.springsecurityexample.todo.Todo;
import com.example.springsecurityexample.todo.dto.TodoDto;
import com.example.springsecurityexample.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public Todo CreateNewTodo (Integer projectId, TodoDto todoDto){
        Todo todo = Todo.builder()
                .projectId(projectId)
                .todoType(todoDto.getTodoType())
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .status(true)
                .build();
        Todo newTodo = todoRepository.save(todo);
        return newTodo;
    }

    public List<Todo> GetTodoListByUserId (Long userId){
        return todoRepository.findAllByUserId(userId);
    }

    public Todo GetTodoByTodoId (Long todoId) {
        return todoRepository.findByTodoId(todoId);
    }

    public Todo endTodo(Long todoId) {
        Todo todo = todoRepository.findByTodoId(todoId);
        todo.setStatus(false);
        return todo;
    }

    public Todo deleteTodo(Long todoId) {
        return todoRepository.deleteVoteByTodoId(todoId);

    }

    public Todo modifyTodo(Long todoId, TodoDto todoDto) {
        Todo newTodo = Todo.builder()
                .todoId(todoId)
                .projectId(todoDto.getProjectId())
                .userId(todoDto.getUserId())
                .todoType(todoDto.getTodoType())
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .status(todoDto.isStatus())
                .build();
        todoRepository.save(newTodo);
        return newTodo;
    }
}
