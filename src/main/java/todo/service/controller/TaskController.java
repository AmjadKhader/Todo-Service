package todo.service.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.dto.request.PatchTaskRequestDto;
import todo.service.dto.request.UpdateTaskRequestDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.service.TaskService;

import java.util.UUID;

@RestController
@RequestMapping("/todo-service/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@PathVariable UUID id, @Valid @RequestBody UpdateTaskRequestDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @PatchMapping("/{id}")
    public TaskResponseDto patchTask(@PathVariable UUID id, @RequestBody PatchTaskRequestDto taskRequestDto) {
        return taskService.patchTask(id, taskRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
