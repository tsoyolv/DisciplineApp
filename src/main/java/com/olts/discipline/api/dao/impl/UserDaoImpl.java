package com.olts.discipline.api.dao.impl;

import com.olts.discipline.api.dao.UserDao;
import com.olts.discipline.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * o.tsoy
 * 26.04.2017
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public User create(User template) {
        return null;
    }

    @Override
    public User get(long id) {
        return null;
    }

    @Override
    @Transactional(transactionManager = "hibernateTransactionManager")
    public User get(String login) {
        final Query query = sessionFactory.getCurrentSession().createQuery("FROM User WHERE login = :login ");
        query.setParameter("login", login);

        return (User) (query.list().isEmpty() ? null : query.list().iterator().next());
    }

    @Override
    @Transactional(transactionManager = "hibernateTransactionManager")
    public Collection<User> get() {
        final Query query = sessionFactory.getCurrentSession().createQuery("FROM User");
        return query.list();
    }

    @Override
    public User update(User template) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}