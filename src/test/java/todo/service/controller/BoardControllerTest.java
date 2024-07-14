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
import todo.service.dto.request.CreateBoardRequestDto;
import todo.service.dto.request.CreateTaskRequestDto;
import todo.service.dto.response.BoardResponseDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.exception.ResourceNotFoundException;
import todo.service.model.TaskStatus;
import todo.service.model.User;
import todo.service.service.BoardService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new ErrorController())
                .build();
    }

    @Test
    void testGetAllBoards() throws Exception {
        BoardResponseDto boardDto = new BoardResponseDto(UUID.randomUUID(), "Test Board", "Board Description", List.of());
        when(boardService.getAllBoards()).thenReturn(Collections.singletonList(boardDto));

        mockMvc.perform(get("/todo-service/api/boards/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(boardDto.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(boardDto.getName()))
                .andExpect(jsonPath("$[0].description").value(boardDto.getDescription()));

        verify(boardService, times(1)).getAllBoards();
        verifyNoMoreInteractions(boardService);
    }

    @Test
    void testCreateBoard() throws Exception {
        CreateBoardRequestDto requestDto = new CreateBoardRequestDto("New Board", "Board Description");

        BoardResponseDto boardDto = new BoardResponseDto(UUID.randomUUID(), requestDto.getName(), requestDto.getDescription(), List.of());
        when(boardService.createBoard(any(CreateBoardRequestDto.class))).thenReturn(boardDto);

        mockMvc.perform(post("/todo-service/api/boards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(boardDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(boardDto.getName()))
                .andExpect(jsonPath("$.description").value(boardDto.getDescription()));

        verify(boardService, times(1)).createBoard(any(CreateBoardRequestDto.class));
        verifyNoMoreInteractions(boardService);
    }

    @Test
    void testCreateBoardValidationFailure() throws Exception {
        CreateBoardRequestDto requestDto = new CreateBoardRequestDto();

        mockMvc.perform(post("/todo-service/api/boards/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(boardService);
    }

    @Test
    void testGetBoardById_Success() throws Exception {
        UUID boardId = UUID.randomUUID();

        BoardResponseDto boardDto = new BoardResponseDto(boardId, "Test Board", "Board Description", List.of());
        when(boardService.getBoardById(boardId)).thenReturn(boardDto);

        mockMvc.perform(get("/todo-service/api/boards/{id}", boardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(boardDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(boardDto.getName()))
                .andExpect(jsonPath("$.description").value(boardDto.getDescription()));

        verify(boardService, times(1)).getBoardById(boardId);
        verifyNoMoreInteractions(boardService);
    }

    @Test
    void testGetBoardById_NotFound() throws Exception {
        UUID boardId = UUID.randomUUID();

        when(boardService.getBoardById(boardId)).thenThrow(new ResourceNotFoundException("Board not found"));

        mockMvc.perform(get("/todo-service/api/boards/{id}", boardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Board not found"));

        verify(boardService, times(1)).getBoardById(boardId);
        verifyNoMoreInteractions(boardService);
    }

    @Test
    void testDeleteBoard() throws Exception {
        UUID boardId = UUID.randomUUID();

        mockMvc.perform(delete("/todo-service/api/boards/{id}", boardId))
                .andExpect(status().isNoContent());

        verify(boardService, times(1)).deleteBoard(boardId);
        verifyNoMoreInteractions(boardService);
    }

    @Test
    void testCreateTask() throws Exception {
        UUID boardId = UUID.randomUUID();
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto("New Task", "Task Description");

        TaskResponseDto taskDto = new TaskResponseDto(UUID.randomUUID(),
                requestDto.getName(),
                requestDto.getDescription(),
                new User(),
                TaskStatus.CREATED.toString()
        );

        when(boardService.createTask(eq(boardId), any(CreateTaskRequestDto.class))).thenReturn(taskDto);

        mockMvc.perform(post("/todo-service/api/boards/{id}/tasks", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskDto.getId().toString()))
                .andExpect(jsonPath("$.name").value(taskDto.getName()))
                .andExpect(jsonPath("$.description").value(taskDto.getDescription()))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(boardService, times(1)).createTask(eq(boardId), any(CreateTaskRequestDto.class));
        verifyNoMoreInteractions(boardService);
    }

    @Test
    void testCreateTaskValidationFailure() throws Exception {
        UUID boardId = UUID.randomUUID();
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();

        mockMvc.perform(post("/todo-service/api/boards/{id}/tasks", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(boardService);
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}