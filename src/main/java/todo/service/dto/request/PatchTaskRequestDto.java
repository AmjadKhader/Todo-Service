package todo.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.service.model.TaskStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchTaskRequestDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String description;
    @NotBlank(message = "User cannot be blank")
    private UUID user;
    @NotBlank(message = "status cannot be blank")
    private TaskStatus status;
}
