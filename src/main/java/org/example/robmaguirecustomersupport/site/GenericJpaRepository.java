package org.example.robmaguirecustomersupport.site;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;

public abstract class GenericJpaRepository<I extends Serializable, E extends Serializable>
        extends GenericBaseRepository<I, E> {
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Iterable<E> getAll() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> query = builder.createQuery(this.entityClass);

        return entityManager.createQuery(query.select(query.from(entityClass))).getResultList();
    }

    @Override
    public E get(I id) {
        return entityManager.find(this.entityClass, id);
    }

    @Override
    public void add(E entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(E entity) {
        entityManager.remove(entity);
    }

    @Override
    public void deleteById(I id) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<E> query = builder.createCriteriaDelete(this.entityClass);

        entityManager.createQuery(query.where(builder.equal(query.from(this.entityClass).get("id"), id))).executeUpdate();
    }
}
