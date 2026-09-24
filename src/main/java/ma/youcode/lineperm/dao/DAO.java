package ma.youcode.lineperm.dao;

public interface DAO<T> {
    void save(T t);
    T findById(long id);
    boolean delete(T t);
}
