package fact.it.carmodelservice.postgresql.spi;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T, I> {
    Optional<T> get(int id);
    Collection<T> getAll();
    Optional<I> save(T t);
    void update(T t);
    void delete(T t);

    Collection<T> getByYear(String year);
    Collection<T> getByType(String type);

    void deleteAll();

    void createDB();
}

