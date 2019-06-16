package com.campus.storage.mysql.internal.engine;

import com.campus.storage.mysql.internal.connection.MysqlConnection;
import com.campus.storage.mysql.internal.resultset.MysqlResultSet;
import com.campus.system.storage.box.Box;
import com.campus.system.storage.box.BoxQuery;
import com.campus.system.storage.connection.ConnectionPool;
import com.campus.system.storage.engine.Engine;
import com.campus.system.storage_annotation.property.Id;
import com.campus.system.storage_annotation.property.Property;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MysqlEngine  extends Engine {
    public <T> long save(T t, Box<T> box, ConnectionPool connectionPool) {
        HashMap<Property, Object> properties = box.saveBean(t);
        String tableName = box.getTableName();
        StringBuilder sqlBuilder = new StringBuilder("insert into " + tableName + " (");
        StringBuilder valueSql = new StringBuilder();
        StringBuilder filedSql = new StringBuilder();
        Iterator<Property> keys = properties.keySet().iterator();
        while (keys.hasNext()){
            Property property = keys.next();
            Object value = properties.get(property);
            String nameInDb = property.getNameInDb();
            if(filedSql.length() > 0){
                filedSql.append(", " + nameInDb);
            }else{
                filedSql.append(nameInDb);
            }

            if(valueSql.length() > 0){
                valueSql.append(", " + (value instanceof String ? "\'"+value+"\'" : value));
            }else{
                valueSql.append(value instanceof String ? "\'"+value+"\'" : value);
            }
        }
        sqlBuilder.append(filedSql.toString());
        sqlBuilder.append(") values (");
        sqlBuilder.append(valueSql.toString());
        sqlBuilder.append(")");
        try {
            MysqlConnection connection = (MysqlConnection) connectionPool.obtain();
            Statement statement = connection.getConnection().createStatement();
            boolean result = statement.execute(sqlBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
            if(result){
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.first()){
                    return resultSet.getInt(1);
                }
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
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
