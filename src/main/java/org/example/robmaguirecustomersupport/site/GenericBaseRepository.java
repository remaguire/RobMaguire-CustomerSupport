package org.example.robmaguirecustomersupport.site;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GenericBaseRepository<I extends Serializable, E extends Serializable>
        implements GenericRepository<I, E> {
    protected final Class<I> idClass;
    protected final Class<E> entityClass;

    public GenericBaseRepository(Class<I> idClass, Class<E> entityClass) {
        this.idClass = idClass;
        this.entityClass = entityClass;
    }

    @SuppressWarnings("unchecked")
    public GenericBaseRepository() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        while (!(genericSuperclass instanceof ParameterizedType)) {
            if (!(genericSuperclass instanceof Class)) {
                throw new IllegalStateException("Unable to determine type " +
                        "arguments because generic superclass neither " +
                        "parameterized type nor class.");
            }

            if (genericSuperclass == GenericBaseRepository.class) {
                throw new IllegalStateException("Unable to determine type " +
                        "arguments because no parameterized generic superclass " +
                        "found.");
            }

            genericSuperclass = ((Class) genericSuperclass).getGenericSuperclass();
        }

        final ParameterizedType type = (ParameterizedType) genericSuperclass;
        final Type[] arguments = type.getActualTypeArguments();
        this.idClass = (Class<I>) arguments[0];
        this.entityClass = (Class<E>) arguments[1];
    }
}
