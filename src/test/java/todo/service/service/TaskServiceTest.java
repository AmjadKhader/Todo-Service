package todo.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import todo.service.dto.request.DeleteUserRequestDto;
import todo.service.dto.request.PatchTaskRequestDto;
import todo.service.dto.request.UpdateTaskRequestDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.exception.ResourceNotFoundException;
import todo.service.model.Task;
import todo.service.model.TaskStatus;
import todo.service.model.User;
import todo.service.repo.TaskRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateTask_Success() {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequestDto taskDto = new UpdateTaskRequestDto();
        taskDto.setName("Updated Task");
        taskDto.setDescription("Updated Description");
        taskDto.setUser(UUID.randomUUID());
        taskDto.setStatus(TaskStatus.STARTED);

        Task task = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        User user = new User();
        when(userService.createUserOrGetIfPresent(any())).thenReturn(user);

        Task updatedTask = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponseDto result = taskService.updateTask(taskId, taskDto);
        assertNotNull(result);
        verify(taskRepository).findById(taskId);
        verify(userService).createUserOrGetIfPresent(any());
        verify(taskRepository).save(task);
    }

    @Test
    void testUpdateTask_NotFound() {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequestDto taskDto = new UpdateTaskRequestDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskId, taskDto));
        verify(taskRepository).findById(taskId);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testPatchTask_Success() {
        UUID taskId = UUID.randomUUID();
        PatchTaskRequestDto taskDto = new PatchTaskRequestDto();
        taskDto.setName("Patched Task");

        Task task = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        User user = new User();
        when(userService.createUserOrGetIfPresent(any())).thenReturn(user);

        Task patchedTask = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(patchedTask);

        TaskResponseDto result = taskService.patchTask(taskId, taskDto);
        assertNotNull(result);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
    }

    @Test
    void testPatchTask_NotFound() {
        UUID taskId = UUID.randomUUID();
        PatchTaskRequestDto taskDto = new PatchTaskRequestDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.patchTask(taskId, taskDto));
        verify(taskRepository).findById(taskId);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testDeleteTasksByUser() {
        UUID userId = UUID.randomUUID();
        DeleteUserRequestDto userDto = new DeleteUserRequestDto();
        userDto.setUser(userId);

        taskService.deleteTasksByUser(userDto);
        verify(taskRepository).deleteByUserId(userId);
    }

    @Test
    void testDeleteTask() {
        UUID taskId = UUID.randomUUID();
        taskService.deleteTask(taskId);
        verify(taskRepository).deleteById(taskId);
    }
}