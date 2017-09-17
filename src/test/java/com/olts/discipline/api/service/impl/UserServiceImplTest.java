package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.service.GroupService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Group;
import com.olts.discipline.entity.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * OLTS on 17.09.2017.
 */
public class UserServiceImplTest {

    @Test
    public void update() throws Exception {

    }

    @Test
    public void get() throws Exception {
        UserService userService = mock(UserService.class);
        User user = new User(1, "user1", "first", 3, 3);
        when(userService.get(1)).thenReturn(user);
        assertEquals(new User(1, "user1", "first", 3, 3), userService.get(1));
    }

    @Test
    public void getByUsername() throws Exception {

    }

    @Test
    public void getByGroup() throws Exception {
        initGroupTest();


    }

    private void initGroupTest() {
        List<User> users = new ArrayList<>(2);
        User user = new User(2, "user2", "first2", 4, 4);
        //userService.create(user);
        User user2 = new User(3, "user3", "first3", 5, 5);
        //userService.create(user2);
        users.add(user);
        users.add(user2);

        Group group1 = new Group(1, "friends");
        group1.setUsers(users);
        group1.setCreatedBy(user);

        GroupService groupService = mock(GroupService.class);
        groupService.create(group1);
    }
}