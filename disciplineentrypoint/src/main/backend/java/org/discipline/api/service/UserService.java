package org.discipline.api.service;


import com.olts.discipline.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    void create(User user);

    void update(User user);

    User get(long userId);

    User getByUsername(String username);

    User getCurrent();

    Page<User> getByGroup(Long groupId, int page, int size);
}
