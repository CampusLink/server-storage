package com.campus.storage.mysql.internal.connection;

import com.campus.system.storage.connection.Connection;
import com.campus.system.storage.connection.ConnectionPool;
import com.campus.system.storage.connection.PoolConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

public class MysqlConnectionPool extends ConnectionPool {
    private ComboPooledDataSource mDataSource;
    public MysqlConnectionPool(PoolConfig config) {
        super(config);
        mDataSource = new ComboPooledDataSource();
        try {
            mDataSource.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        mDataSource.setJdbcUrl(config.getUrl() + "/" + config.getStoreName());
        mDataSource.setUser(config.getUserName());
        mDataSource.setPassword(config.getPassword());
        mDataSource.setInitialPoolSize(config.getInitPoolSize());
        mDataSource.setMinPoolSize(config.getMinPoolSize());
        mDataSource.setMaxPoolSize(config.getMaxPoolSize());
    }

    public Connection obtain() throws SQLException{
        return new MysqlConnection(mDataSource.getConnection());
    }

    public void release(Connection connection) throws SQLException{
        MysqlConnection mysqlConnection = (MysqlConnection) connection;
        mysqlConnection.getConnection().close();
    }
}
