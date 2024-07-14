package todo.service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.service.dto.request.DeleteUserRequestDto;
import todo.service.service.TaskService;

@RestController
@RequestMapping("/todo-service/api/webhooks")
public class WebhookController {

    private final TaskService taskService;

    public WebhookController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/user-deleted")
    public void handleUserDeleted(@RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        taskService.deleteTasksByUser(deleteUserRequestDto);
    }
}
