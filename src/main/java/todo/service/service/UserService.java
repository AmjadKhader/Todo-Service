package todo.service.service;

import org.springframework.stereotype.Service;
import todo.service.model.User;
import todo.service.repo.UserRepository;
import todo.service.util.UserUtil;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserUtil userUtil;

    public UserService(UserRepository userRepository, UserUtil userUtil) {
        this.userRepository = userRepository;
        this.userUtil = userUtil;
    }

    public User createUser() {
        User user = new User();
        user.setName(userUtil.getUserDetails(user.getId()).getName());
        return userRepository.save(user);
    }

    public User createUserOrGetIfPresent(UUID userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User user = new User();
        user.setName(userUtil.getUserDetails(userId).getName());
        return userRepository.save(user);
    }

}
