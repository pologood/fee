package com.sogou.pay.fee.mapper;

import com.sogou.pay.fee.entity.Provider;
import commons.mybatis.Paging;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by nahongxu on 2016/6/22.
 */
public interface ProviderMapper {
    class Sql {
        public static final String TABLE = "providers";
        public static final String SELECT_BY_PROVIDERID = "SELECT * FROM " + TABLE + " WHERE providerId = #{providerId}";
        public static final String UPDATE_STATUS = "UPDATE " + TABLE + " SET status = #{status}";
        public static final String DELETE = "DELETE FROM " + TABLE + " SET status = #{status}";

        public static String insert(Provider provider) {
            SQL sql = new SQL().INSERT_INTO(TABLE)
                    .VALUES("providerName", "#{providerName}")
                    .VALUES("payChannel", "#{payChannel}")
                    .VALUES("status", "#{status}");
            if (provider.getProviderDesc() != null) {
                sql.VALUES("providerDesc", "#{providerDesc}");
            }
            return sql.toString();
        }
    }

    @InsertProvider(type = Sql.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "providerId")
    int add(Provider provider);

    @Select(Sql.SELECT_BY_PROVIDERID)
    Provider findByProviderId(long providerId);

    @SelectProvider(type = Paging.class, method = "list")
    List<Provider> find(Paging paging);

    @SelectProvider(type = Paging.class, method = "first")
    List<Long> first(Paging paging);

    @SelectProvider(type = Paging.class, method = "forward")
    List<Long> forward(Paging paging);

    @SelectProvider(type = Paging.class, method = "backward")
    List<Long> backward(Paging paging);

    @Update(Sql.UPDATE_STATUS)
    int updateStatus(@Param("providerId") long providerId, @Param("status") Provider.Status status);


    @Delete(Sql.DELETE)
    int delete(@Param("providerId") long providerId, @Param("status") Provider.Status status);


}
