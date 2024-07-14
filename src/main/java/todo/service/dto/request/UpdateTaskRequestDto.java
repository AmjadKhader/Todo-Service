package todo.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.service.model.TaskStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequestDto {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private UUID user;
    @NotNull
    private TaskStatus status;
}
