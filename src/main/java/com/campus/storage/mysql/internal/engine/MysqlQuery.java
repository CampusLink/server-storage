package com.campus.storage.mysql.internal.engine;

import com.campus.system.storage.box.BoxQuery;
import com.campus.system.storage.filter.*;
import com.campus.system.storage_annotation.property.Property;

import java.util.Date;
import java.util.List;

public class MysqlQuery {
    public static <T> String convertToSql(BoxQuery<T> query){
        //TODO BoxQuery转化为sql语句
        StringBuilder sqlBuilder = new StringBuilder("select ");
        List<Property> keys = query.getKeys();
        for(Property property : keys){
            sqlBuilder.append(property.getNameInDb() + ",");
        }
        if(keys.size() > 0){
            int length = sqlBuilder.length();
            sqlBuilder.delete(length - 1, length);
        }else{
            sqlBuilder.append(" * ");
        }
        sqlBuilder.append(" from " + query.getBoxName());
        List<IFilter> filters = query.getFilters();
        if(filters.size() > 0){
            sqlBuilder.append(" where ");
        }
        for(int i = 0, n = filters.size(); i < n; i++){
            IFilter filter = filters.get(i);
            sqlBuilder.append(parseFilter(filter));
            if(i < n-1){
                sqlBuilder.append(" and ");
            }
        }
        if(query.getLimit() > 0){
            sqlBuilder.append(" limit " + query.getLimit());
        }
        if(query.getOrderAsc() != null){
            sqlBuilder.append(" order by " + query.getOrderAsc().getNameInDb() + " asc");
        }else if(query.getOrderDesc() != null){
            sqlBuilder.append(" order by " + query.getOrderDesc().getNameInDb() + " desc");
        }
        return sqlBuilder.toString();
    }

    private static String parseFilter(IFilter filter){
        if(filter instanceof BetweenFilter){
            //TODO between语意需要确认
            BetweenFilter betweenFilter = (BetweenFilter) filter;
            StringBuilder str = new StringBuilder(betweenFilter.getKey() + " between ");
            str.append(betweenFilter.getLeft());
            str.append(" and ");
            str.append(betweenFilter.getRight());
            return str.toString();
        }else if(filter instanceof EndWithFilter){
            EndWithFilter endWithFilter = (EndWithFilter) filter;
            return endWithFilter.getKey() + " like \'%" + endWithFilter.getValue() + "\'";
        }else if(filter instanceof StartWithFilter){
            StartWithFilter startWithFilter = (StartWithFilter) filter;
            return startWithFilter.getKey() + " like \'" + startWithFilter.getValue() + "%\'";
        }else if(filter instanceof ContainsFilter){
            ContainsFilter containsFilter = (ContainsFilter) filter;
            return containsFilter.getKey() + " \"%" + containsFilter.getValue() + "%\"";
        }else if(filter instanceof EqualToFilter){
            EqualToFilter equalToFilter = (EqualToFilter) filter;
            Object value = equalToFilter.getValue();
            if(value instanceof String){
                return equalToFilter.getKey() + "=\'" + value + "\'";
            }else{
                return dealNumber(value, equalToFilter.getKey(), "=");
            }
        }else if(filter instanceof GreaterThanEqualToFilter){
            GreaterThanEqualToFilter greaterThanEqual = (GreaterThanEqualToFilter) filter;
            Object value = greaterThanEqual.getValue();
            return dealNumber(value, greaterThanEqual.getKey(), ">=");
        }else if(filter instanceof GreaterThanFilter){
            GreaterThanFilter greaterThan = (GreaterThanFilter) filter;
            Object value = greaterThan.getValue();
            return dealNumber(value, greaterThan.getKey(), ">");
        }else if(filter instanceof LessThanEqualToFilter){
            LessThanEqualToFilter lessThanEqualToFilter = (LessThanEqualToFilter) filter;
            return lessThanEqualToFilter.getKey() + "<=" + lessThanEqualToFilter.getValue();
        }else if(filter instanceof LessThanFilter){
            LessThanFilter lessThanFilter = (LessThanFilter) filter;
            return lessThanFilter.getKey() + "<" + lessThanFilter.getValue();
        }else if(filter instanceof NearFilter){

        }else if(filter instanceof NearWithInKilometersFilter){

        }else if(filter instanceof ContainedInFilter){
            ContainedInFilter containedInFilter = (ContainedInFilter) filter;
            StringBuilder str = new StringBuilder(containedInFilter.getKey() + " in ");
            for(int i = 0, n = containedInFilter.getValue().size(); i < n; i++){
                Object item = containedInFilter.getValue().get(i);
                if(item instanceof String){
                    if(i < n - 1){
                        str.append("\"" + item + "\"");
                    }else{
                        str.append("\"" + item + "\" and ");
                    }
                }
            }
            return str.toString();
        }else if(filter instanceof NotContainedInFilter){
            NotContainedInFilter notContainedInFilter = (NotContainedInFilter) filter;
            StringBuilder str = new StringBuilder(notContainedInFilter.getKey() + " not in ");
            for(int i = 0, n = notContainedInFilter.getValue().size(); i < n; i++){
                Object item = notContainedInFilter.getValue().get(i);
                if(item instanceof String){
                    if(i < n - 1){
                        str.append("\"" + item + "\"");
                    }else{
                        str.append("\"" + item + "\" and ");
                    }
                }
            }
            return str.toString();
        }else if(filter instanceof NotEqualToFilter){
            EqualToFilter equalToFilter = (EqualToFilter) filter;
            Object value = equalToFilter.getValue();
            if(value instanceof String){
                return equalToFilter.getKey() + "!=\'" + value + "\'";
            }else{
                return dealNumber(value, equalToFilter.getKey(), "!=");
            }
        }

        return " ";
    }

    private static String dealNumber(Object value, String key, String tag){
        if(value instanceof Integer){
            return key + tag + Integer.parseInt(value.toString());
        }else if(value instanceof Long){
            return key + tag + Long.parseLong(value.toString());
        }else if(value instanceof Float){
            return key + tag + Float.parseFloat(value.toString());
        }else if(value instanceof Double){
            return key + tag + Double.parseDouble(value.toString());
        }

        return "";
    }
}
