package todo.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String status;
}
