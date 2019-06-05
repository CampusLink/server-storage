package com.campus.storage.mysql.internal.engine;

import com.campus.storage.mysql.internal.connection.MysqlConnection;
import com.campus.storage.mysql.internal.resultset.MysqlResultSet;
import com.campus.system.storage.box.Box;
import com.campus.system.storage.box.BoxQuery;
import com.campus.system.storage.connection.ConnectionPool;
import com.campus.system.storage.engine.Engine;
import com.campus.system.storage_annotation.property.Id;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class MysqlEngine  extends Engine {
    public <T> T save(T t, Box<T> box, ConnectionPool connectionPool) {
        return null;
    }

    public <T> T remove(T t, Box<T> box, ConnectionPool connectionPool) {
        return null;
    }

    public <T> T update(T t, Box<T> box, ConnectionPool connectionPool) {
        return null;
    }

    public <T> T get(Id aLong, Box<T> box, ConnectionPool connectionPool) {
        return null;
    }

    public <T> List<T> find(BoxQuery<T> boxQuery, Box<T> box, ConnectionPool connectionPool) {
        try {
            MysqlConnection connection = (MysqlConnection) connectionPool.obtain();
            Statement statement = connection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(MysqlQuery.convertToSql(boxQuery));
            MysqlResultSet resultSet = new MysqlResultSet(result);
            return box.parseResult(resultSet, boxQuery.getKeys());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
