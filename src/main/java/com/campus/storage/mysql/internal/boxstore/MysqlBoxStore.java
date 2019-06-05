package com.campus.storage.mysql.internal.boxstore;

import com.campus.storage.mysql.internal.connection.MysqlConnectionPool;
import com.campus.system.storage.box.BoxStore;
import com.campus.system.storage.connection.ConnectionPool;
import com.campus.system.storage.connection.PoolConfig;
import com.campus.system.storage.engine.Engine;

public class MysqlBoxStore extends BoxStore {
    private ConnectionPool mPool;
    public MysqlBoxStore(PoolConfig config, Engine engine) {
        super(config, engine);
    }

    public ConnectionPool withConfig(PoolConfig poolConfig) {
        if(mPool == null){
            mPool = new MysqlConnectionPool(poolConfig);
        }
        return mPool;
    }
}
