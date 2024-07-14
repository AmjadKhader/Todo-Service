package todo.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.service.dto.response.BoardResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "boards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public BoardResponseDto toDto() {
        BoardResponseDto boardResponseDto = new BoardResponseDto();
        boardResponseDto.setId(getId());
        boardResponseDto.setName(getName());
        boardResponseDto.setDescription(getDescription());
        boardResponseDto.setTasks(getTasks().stream().map(Task::toDto).collect(Collectors.toList()));
        return boardResponseDto;
    }
}
