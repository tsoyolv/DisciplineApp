package com.olts.discipline.api.dao.impl;

import com.olts.discipline.api.dao.HabitDao;
import com.olts.discipline.model.Habit;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;

/**
 * o.tsoy
 * 25.04.2017
 */
@Repository("habitDao")
@EnableTransactionManagement
public class HabitDaoImpl implements HabitDao {

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public Habit create(Habit template) {
        return null;
    }

    @Override
    @Transactional(transactionManager = "hibernateTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public Habit get(long id) {
        return sessionFactory.getCurrentSession().get(Habit.class, id);
    }

    @Override
    @Transactional(transactionManager = "hibernateTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public Collection<Habit> get() {
        final CriteriaQuery<Habit> query = sessionFactory.getCurrentSession().getCriteriaBuilder().createQuery(Habit.class);
        return sessionFactory.getCurrentSession().createQuery(query).list();
    }

    @Override
    public Habit update(Habit template) {
        return null;
    }

    @Override
    public Habit delete(long id) {
        return null;
    }
}
