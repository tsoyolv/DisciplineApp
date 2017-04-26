package com.olts.discipline.api.dao;

import com.olts.discipline.model.User;

import java.util.Collection;

/**
 * o.tsoy
 * 26.04.2017
 */
public interface UserDao {

    User create(User template);

    User get(long id);

    User get(String login);

    Collection<User> get();

    User update(User template);

    void delete(long id);
}
