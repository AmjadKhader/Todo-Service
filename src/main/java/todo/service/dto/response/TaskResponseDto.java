package todo.service.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import todo.service.model.User;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private UUID id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private User user;
    private String status;
}
