package todo.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.service.dto.response.TaskResponseDto;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public TaskResponseDto toDto() {
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(getId());
        taskResponseDto.setName(getName());
        taskResponseDto.setDescription(getDescription());
        taskResponseDto.setUser(getUser());
        taskResponseDto.setStatus(getStatus());
        return taskResponseDto;
    }
}
