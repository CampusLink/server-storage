package com.campus.storage.mysql.internal.connection;

import com.campus.system.storage.connection.Connection;

public class MysqlConnection extends Connection {
    private java.sql.Connection mConnection;

    public MysqlConnection(java.sql.Connection connection) {
        mConnection = connection;
    }

    public java.sql.Connection getConnection() {
        return mConnection;
    }
}
