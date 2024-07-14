package todo.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import todo.service.model.User;
import todo.service.repo.UserRepository;
import todo.service.util.UserUtil;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUtil userUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UUID userId = UUID.randomUUID();
        String userName = "Test User";

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName(userName);

        when(userUtil.getUserDetails(any())).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser();

        verify(userRepository).save(any(User.class));

        assertNotNull(createdUser);
        assertEquals(userId, createdUser.getId());
        assertEquals(userName, createdUser.getName());
    }

    @Test
    void testCreateUserOrGetIfPresentNewUser() {
        UUID userId = UUID.randomUUID();
        String userName = "Test User";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User user = new User(userId, userName);
        when(userUtil.getUserDetails(userId)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdOrExistingUser = userService.createUserOrGetIfPresent(userId);

        verify(userRepository).findById(userId);

        verify(userRepository).save(any(User.class));

        assertNotNull(createdOrExistingUser);
        assertEquals(userId, createdOrExistingUser.getId());
        assertEquals(userName, createdOrExistingUser.getName());
    }

    @Test
    void testCreateUserOrGetIfPresentExistingUser() {
        UUID userId = UUID.randomUUID();
        String existingUserName = "Existing User";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName(existingUserName);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User createdOrExistingUser = userService.createUserOrGetIfPresent(userId);

        verify(userRepository).findById(userId);

        verifyNoMoreInteractions(userRepository);

        assertNotNull(createdOrExistingUser);
        assertEquals(userId, createdOrExistingUser.getId());
        assertEquals(existingUserName, createdOrExistingUser.getName());
    }
}
