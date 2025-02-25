package todo.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequestDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotNull
    private String description;
    @NotNull
    private UUID user;
    @NotBlank
    private String status;
}
