package com.campus.storage.mysql;

import com.campus.storage.mysql.internal.boxstore.MysqlBoxStore;
import com.campus.storage.mysql.internal.engine.MysqlEngine;
import com.campus.system.ServiceContext;
import com.campus.system.ServiceMenu;
import com.campus.system.annotation.Service;
import com.campus.system.menu.User_;
import com.campus.system.storage.StorageService;
import com.campus.system.storage.box.Box;
import com.campus.system.storage.box.BoxQuery;
import com.campus.system.storage.box.BoxStore;
import com.campus.system.storage.connection.PoolConfig;
import com.campus.system.storage.engine.Engine;
import com.campus.system.storage_annotation.property.Property;
import com.campus.system.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.campus.storage.mysql.internal.engine.MysqlQuery.convertToSql;

@Service(name = ServiceMenu.STORAGE_MYSQL, module = "MysqlStorage")
public class StorageServiceImpl extends StorageService {
    private ServiceContext mContext;
    private HashMap<String, BoxStore> mBoxStores;
    private Engine mEngine;

    public void init(ServiceContext serviceContext) {
        mContext = serviceContext;
        mBoxStores = new HashMap<String, BoxStore>();
        mEngine = new MysqlEngine();
    }

    public BoxStore obtainBoxStore(String url, String storeName, String userName, String password) {
        String key = url + storeName + userName + password;
        if(mBoxStores.containsKey(key)){
            return mBoxStores.get(key);
        }
        PoolConfig config = PoolConfig.defaultConfig(url, storeName, userName, password);
        BoxStore boxStore = new MysqlBoxStore(config, mEngine);
        mBoxStores.put(key, boxStore);
        return boxStore;
    }

    public static void main(String[] args){
        List<Property> properties = new ArrayList<Property>();
        properties.add(User_.mId);
        properties.add(User_.mName);
        properties.add(User_.mPhone);
        properties.add(User_.mBirth);
        StorageService storageService = new StorageServiceImpl();
        storageService.init(null);
        BoxStore store = storageService.obtainBoxStore("jdbc:mysql://192.168.31.140:3306", "SmallChange", "root", null);
        Box<User> box = store.boxFor(User.class);
        BoxQuery query = box.obtainQuery();
        query
                //.whereGreaterThan(User_.mBirth, System.currentTimeMillis())
                .whereEqualTo(User_.mName, "liuji")
                .setKeys(properties);
        List<User> users = box.find(query);
        System.out.println(convertToSql(query));
        System.out.println(users.size());
    }
}
