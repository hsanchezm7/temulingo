package es.um.pds.temulingo.dao.base;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

	T edit(T obj);

	Optional<T> get(long id);

	List<T> getAll();

	T save(T t);

	void delete(T t);
}
