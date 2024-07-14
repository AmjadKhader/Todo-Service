package todo.service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.service.dto.request.CreateBoardRequestDto;
import todo.service.dto.request.CreateTaskRequestDto;
import todo.service.dto.response.BoardResponseDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.exception.ResourceNotFoundException;
import todo.service.model.Board;
import todo.service.model.Task;
import todo.service.model.TaskStatus;
import todo.service.model.User;
import todo.service.repo.BoardRepository;
import todo.service.repo.TaskRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public BoardService(BoardRepository boardRepository, TaskRepository taskRepository, UserService userService) {
        this.boardRepository = boardRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll().stream().map(Board::toDto).collect(Collectors.toList());
    }

    public BoardResponseDto createBoard(CreateBoardRequestDto boardDto) {
        Board board = new Board();
        board.setName(boardDto.getName());
        board.setDescription(boardDto.getDescription());
        return boardRepository.save(board).toDto();
    }

    public BoardResponseDto getBoardById(UUID id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        return board.toDto();
    }

    @Transactional
    public void deleteBoard(UUID id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public TaskResponseDto createTask(UUID boardId, CreateTaskRequestDto taskDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        User user = userService.createUser();

        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.CREATED.toString());
        task.setUser(user);
        task.setBoard(board);

        return taskRepository.save(task).toDto();
    }
}
