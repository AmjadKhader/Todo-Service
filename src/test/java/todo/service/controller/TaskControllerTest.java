package todo.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import todo.service.dto.request.PatchTaskRequestDto;
import todo.service.dto.request.UpdateTaskRequestDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.exception.ResourceNotFoundException;
import todo.service.model.TaskStatus;
import todo.service.service.TaskService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new ErrorController())
                .build();
    }

    @Test
    void testUpdateTask_ValidInput() throws Exception {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequestDto requestDto = new UpdateTaskRequestDto(
                "Updated Task",
                "Updated Description",
                UUID.randomUUID(),
                TaskStatus.COMPLETED
        );

        TaskResponseDto responseDto = new TaskResponseDto(taskId,
                "Updated Task",
                "Updated Description",
                any(),
                "COMPLETED");

        when(taskService.updateTask(taskId, any(UpdateTaskRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/todo-service/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(taskService, times(1)).updateTask(eq(taskId), any(UpdateTaskRequestDto.class));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testUpdateTask_InvalidInput() throws Exception {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequestDto requestDto = new UpdateTaskRequestDto(
                null,
                "Updated Description",
                UUID.randomUUID(),
                TaskStatus.COMPLETED
        );

        mockMvc.perform(put("/todo-service/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(taskService);
    }

    @Test
    void testPatchTask_ValidInput() throws Exception {
        UUID taskId = UUID.randomUUID();
        PatchTaskRequestDto requestDto = new PatchTaskRequestDto("Updated Task", null, null, null);

        TaskResponseDto responseDto = new TaskResponseDto(
                taskId,
                "Updated Task",
                "Initial Description",
                any(),
                "STARTED"
        );

        when(taskService.patchTask(taskId, any(PatchTaskRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/todo-service/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Initial Description"))
                .andExpect(jsonPath("$.status").value("STARTED"));

        verify(taskService, times(1)).patchTask(eq(taskId), any(PatchTaskRequestDto.class));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testDeleteTask_ValidInput() throws Exception {
        UUID taskId = UUID.randomUUID();

        mockMvc.perform(delete("/todo-service/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(eq(taskId));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testDeleteTask_TaskNotFound() throws Exception {
        UUID taskId = UUID.randomUUID();

        doThrow(new ResourceNotFoundException("Task not found")).when(taskService).deleteTask(eq(taskId));

        mockMvc.perform(delete("/todo-service/api/tasks/{id}", taskId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));

        verify(taskService, times(1)).deleteTask(eq(taskId));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void testUpdateTask_ServiceException() throws Exception {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequestDto requestDto = new UpdateTaskRequestDto(
                "Updated Task",
                "Updated Description",
                UUID.randomUUID(),
                TaskStatus.COMPLETED
        );

        when(taskService.updateTask(taskId, requestDto))
                .thenThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(put("/todo-service/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));

        verify(taskService, times(1)).updateTask(eq(taskId), any(UpdateTaskRequestDto.class));
        verifyNoMoreInteractions(taskService);
    }
}
