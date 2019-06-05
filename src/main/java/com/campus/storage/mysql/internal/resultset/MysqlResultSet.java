package com.campus.storage.mysql.internal.resultset;

import com.campus.system.storage.ResultSet.ResultSet;

import java.sql.SQLException;
import java.util.Date;

public class MysqlResultSet extends ResultSet {
    private java.sql.ResultSet mResultSet;

    public MysqlResultSet(java.sql.ResultSet resultSet) {
        mResultSet = resultSet;
    }

    public String getString(int i) throws SQLException{
        return mResultSet.getString(i);
    }

    public boolean getBoolean(int i) throws SQLException{
        return mResultSet.getBoolean(i);
    }

    public long getId(int i) throws SQLException{
        return mResultSet.getLong(i);
    }

    public Date getDate(int i) throws SQLException{
        return new Date(mResultSet.getLong(i));
    }

    public double getDouble(int i) throws SQLException{
        return mResultSet.getDouble(i);
    }

    public int getInt(int i) throws SQLException{
        return mResultSet.getInt(i);
    }

    public long getLong(int i) throws SQLException{
        return mResultSet.getLong(i);
    }

    public int findColumn(String s) throws SQLException{
        return mResultSet.findColumn(s);
    }

    public boolean next() throws SQLException {
        return mResultSet.next();
    }
}
