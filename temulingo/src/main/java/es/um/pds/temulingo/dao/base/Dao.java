package es.um.pds.temulingo.dao.base;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

	void edit(T obj);

	Optional<T> get(long id);

	List<T> getAll();

	void save(T t);

	void delete(T t);
}
