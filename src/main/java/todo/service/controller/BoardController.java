package todo.service.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.dto.request.CreateBoardRequestDto;
import todo.service.dto.request.CreateTaskRequestDto;
import todo.service.dto.response.BoardResponseDto;
import todo.service.dto.response.TaskResponseDto;
import todo.service.service.BoardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todo-service/api/boards/")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public List<BoardResponseDto> getAllBoards() {
        return boardService.getAllBoards();
    }

    @PostMapping("/")
    public BoardResponseDto createBoard(@Valid @RequestBody CreateBoardRequestDto createBoardRequestDto) {
        return boardService.createBoard(createBoardRequestDto);
    }

    @GetMapping("/{id}")
    public BoardResponseDto getBoardById(@PathVariable UUID id) {
        return boardService.getBoardById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable UUID id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tasks")
    public TaskResponseDto createTask(@PathVariable UUID id, @Valid @RequestBody CreateTaskRequestDto taskDto) {
        return boardService.createTask(id, taskDto);
    }
}
