package me.don1ns.learnlink.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class BaseDAO<T> {
    protected Connection connection;

    public BaseDAO(Connection connection) {
        this.connection = connection;
    }

    public abstract void create(T entity) throws SQLException;

    public abstract List<T> getAll() throws SQLException;

    public abstract Optional<T> getById(Long id) throws SQLException;

    public abstract void update(T entity) throws SQLException;

    public abstract void deleteById(Long id) throws SQLException;
}
