package todo.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import todo.service.dto.request.DeleteUserRequestDto;
import todo.service.service.TaskService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WebhookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private WebhookController webhookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(webhookController).build();
    }

    @Test
    void testHandleUserDeleted_ValidRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        DeleteUserRequestDto requestDto = new DeleteUserRequestDto(userId);

        mockMvc.perform(post("/todo-service/api/webhooks/user-deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTasksByUser(eq(requestDto));
        verifyNoMoreInteractions(taskService);
    }

}
