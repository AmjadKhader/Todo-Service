package todo.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import todo.service.dto.request.CreateBoardRequestDto;
import todo.service.dto.request.CreateTaskRequestDto;
import todo.service.dto.response.BoardResponseDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.exception.ResourceNotFoundException;
import todo.service.model.Board;
import todo.service.model.Task;
import todo.service.model.User;
import todo.service.repo.BoardRepository;
import todo.service.repo.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBoards() {
        List<Board> boards = List.of(new Board(), new Board());
        when(boardRepository.findAll()).thenReturn(boards);

        List<BoardResponseDto> result = boardService.getAllBoards();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(boardRepository).findAll();
    }

    @Test
    void testCreateBoard() {
        CreateBoardRequestDto boardDto = new CreateBoardRequestDto();
        boardDto.setName("New Board");
        boardDto.setDescription("Board Description");

        Board board = new Board();
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        BoardResponseDto result = boardService.createBoard(boardDto);
        assertNotNull(result);
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void testGetBoardById_Success() {
        UUID boardId = UUID.randomUUID();
        Board board = new Board();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        BoardResponseDto result = boardService.getBoardById(boardId);
        assertNotNull(result);
        verify(boardRepository).findById(boardId);
    }

    @Test
    void testGetBoardById_NotFound() {
        UUID boardId = UUID.randomUUID();
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> boardService.getBoardById(boardId));
        verify(boardRepository).findById(boardId);
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    void testDeleteBoard() {
        UUID boardId = UUID.randomUUID();
        boardService.deleteBoard(boardId);
        verify(boardRepository).deleteById(boardId);
    }

    @Test
    void testCreateTask_Success() {
        UUID boardId = UUID.randomUUID();
        CreateTaskRequestDto taskDto = new CreateTaskRequestDto();
        taskDto.setName("New Task");
        taskDto.setDescription("Task Description");

        Board board = new Board();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        User user = new User();
        when(userService.createUser()).thenReturn(user);

        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto result = boardService.createTask(boardId, taskDto);
        assertNotNull(result);
        verify(boardRepository).findById(boardId);
        verify(userService).createUser();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testCreateTaskBoard_NotFound() {
        UUID boardId = UUID.randomUUID();
        CreateTaskRequestDto taskDto = new CreateTaskRequestDto();

        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> boardService.createTask(boardId, taskDto));
        verify(boardRepository).findById(boardId);
        verifyNoMoreInteractions(boardRepository);
    }
}