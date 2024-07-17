package todo.service.dto.response;

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
    private String name;
    private String description;
    private User user;
    private String status;
}
