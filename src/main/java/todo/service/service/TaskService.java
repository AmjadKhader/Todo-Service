package todo.service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import todo.service.dto.request.DeleteUserRequestDto;
import todo.service.dto.request.PatchTaskRequestDto;
import todo.service.dto.request.UpdateTaskRequestDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.exception.ResourceNotFoundException;
import todo.service.model.Task;
import todo.service.model.TaskStatus;
import todo.service.model.User;
import todo.service.repo.TaskRepository;

import java.util.Objects;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional
    public TaskResponseDto updateTask(UUID id, UpdateTaskRequestDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        User user = userService.createUserOrGetIfPresent(taskDto.getUser());

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.fromString(taskDto.getStatus()).toString());
        task.setUser(user);
        return taskRepository.save(task).toDto();
    }

    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskResponseDto patchTask(UUID id, PatchTaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        if (Objects.nonNull(taskRequestDto.getName())) {
            task.setName(taskRequestDto.getName());
        }
        if (Objects.nonNull(taskRequestDto.getDescription())) {
            task.setDescription(taskRequestDto.getDescription());
        }
        if (Objects.nonNull(taskRequestDto.getUser())) {
            task.setUser(userService.createUserOrGetIfPresent(taskRequestDto.getUser()));
        }
        if (Objects.nonNull(TaskStatus.fromString(taskRequestDto.getStatus()).toString())) {
            task.setStatus(taskRequestDto.getStatus());
        }
        return taskRepository.save(task).toDto();
    }

    @Transactional
    public void deleteTasksByUser(DeleteUserRequestDto user) {
        taskRepository.deleteByUserId(user.getUser());
    }
}
