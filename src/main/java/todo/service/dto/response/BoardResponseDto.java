package todo.service.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {
    private UUID id;
    @NotBlank
    private String name;
    private String description;
    private List<TaskResponseDto> tasks;
}
