package todo.service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import todo.service.model.User;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserUtilTest {

    @InjectMocks
    private UserUtil userUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserDetails_Success() {
        UUID userId = UUID.randomUUID();
        User user = userUtil.getUserDetails(userId);

        assertEquals(userId, user.getId());
        assertFalse(user.getName().isEmpty());
    }
}
