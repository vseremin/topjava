package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> userRepository = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    {
        save(new User(1, "userName", "email@mail.ru", "password", Role.USER));
        save(new User(2, "adminName", "email@mail.ru", "password", Role.ADMIN));
    }

    @Override
    public boolean delete(int id) {
        if (userRepository.remove(id) != null) {
            log.info("delete {}", id);
            return true;
        }
        log.info("user with id {} does not exist", id);
        return false;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(id.incrementAndGet());
            log.info("save {}", user);
            userRepository.put(user.getId(), user);
            return user;
        }
        log.info("update {}", user);
        return userRepository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        User user = userRepository.get(id);
        if (user != null) {
            log.info("get {}", id);
            return user;
        }
        log.info("user with id {} does not exist", id);
        return null;
    }

    @Override
    public List<User> getAll() {
        if (!userRepository.isEmpty()) {
            log.info("getAll");
            return userRepository.values().stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toList());
        }
        log.info("user repository is empty");
        return Collections.emptyList();
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        if (user != null) {
            log.info("getByEmail {}", email);
            return user;
        }
        log.info("user with email {} does not exist", email);
        return null;
    }
}
