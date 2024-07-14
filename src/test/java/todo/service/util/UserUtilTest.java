package todo.service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import todo.service.model.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserUtil userUtil;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetUserDetails_Success() {
        UUID userId = UUID.randomUUID();

        when(restTemplate.getForObject("https://randomuser.me/api/", Map.class))
                .thenReturn(Map.of(
                        "results", List.of(Map.of(
                                "name", Map.of(
                                        "first", "John",
                                        "last", "Doe"
                                )
                        ))));

        User user = userUtil.getUserDetails(userId);

        assertEquals(userId, user.getId());
        assertEquals("John Doe", user.getName());
    }
}
