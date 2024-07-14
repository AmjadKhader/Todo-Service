package todo.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.service.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}