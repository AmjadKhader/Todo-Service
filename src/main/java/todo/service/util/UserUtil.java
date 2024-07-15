package todo.service.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import todo.service.model.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class UserUtil {

    private final RestTemplate restTemplate = new RestTemplate();

    public User getUserDetails(UUID userId) {
        Map<String, Object> response = restTemplate.getForObject("https://randomuser.me/api/", Map.class);
        Map<String, Object> user = ((List<Map<String, Object>>) response.get("results")).get(0);
        Map<String, String> name = (Map<String, String>) user.get("name");
        String fullName = name.get("first") + " " + name.get("last");
        return new User(userId, fullName);
    }
}
