package com.example.springsecurityexample.todo.controller;

import com.example.springsecurityexample.todo.Todo;
import com.example.springsecurityexample.todo.dto.TodoDto;
import com.example.springsecurityexample.todo.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor //DI
@RequestMapping(value = "/todo", produces = MediaTypes.HAL_JSON_VALUE)
public class TodoController {

    private final TodoService todoService;

    @ApiOperation("나의 모든 투두리스트 조회")
    @GetMapping("/lists/{userId}")
    public ResponseEntity<List<Todo>> ReadAllLists (@PathVariable Long userId){
        List<Todo> todoList = todoService.GetTodoListByUserId(userId);
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @ApiOperation("모든 투두리스트 중 하나를 선택")
    @GetMapping("/lists/{userId}/list/{todoId}")
    public ResponseEntity<Todo> ReadList (@PathVariable Long todoId){
        Todo todo = todoService.GetTodoByTodoId(todoId);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @ApiOperation("투두리스트 생성 \nstatus가 true일때 해당 Todo가 진행중인 상태")
    @PostMapping("/new/{projectId}")
    public ResponseEntity<Todo> CreateTodo (@PathVariable Integer projectId, @RequestBody TodoDto todoDto){
        Todo createdTodo = todoService.CreateNewTodo(projectId, todoDto);
        return new ResponseEntity<>(createdTodo, HttpStatus.OK);
    }

    @ApiOperation("특정 투두리스트 종료\nstatus = false로 변경한다.\nstatus가 false일때 해당 Todo가 완료된 상태")
    @PatchMapping("/end/{todoId}")
    public ResponseEntity<Todo> EndTodo(@PathVariable Long todoId) {
        Todo endTodo = todoService.endTodo(todoId);
        return new ResponseEntity<>(endTodo, HttpStatus.ACCEPTED);
    }

    @ApiOperation("투두리스트 내용 변경")
    @PatchMapping("/change/{todoId}")
    public ResponseEntity<Todo> ModifyTodo(@PathVariable Long todoId, @RequestBody TodoDto todoDto) {
        Todo modifyTodo = todoService.modifyTodo(todoId, todoDto);
        return new ResponseEntity<>(modifyTodo, HttpStatus.ACCEPTED);
    }

//     TODO : @DeleteMapping, ToDoList를 삭제할 필요가 있을까?, 필요할 것 같아서 올렸던 사항이 필요없어졌다고 생각되면?
    @ApiOperation("투두리스트 삭제")
    @DeleteMapping("/remove/{todoId}")
    public ResponseEntity<Todo> DeleteTodo(@PathVariable Long todoId) {
        Todo deleteTodo = todoService.deleteTodo(todoId);
        return new ResponseEntity<>(deleteTodo, HttpStatus.ACCEPTED);
    }
}
