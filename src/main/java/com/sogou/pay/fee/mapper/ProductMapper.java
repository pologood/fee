package com.sogou.pay.fee.mapper;

import com.sogou.pay.fee.entity.Product;
import commons.mybatis.Paging;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by nahongxu on 2016/6/22.
 */
public interface ProductMapper {
    class Sql {
        public static final String TABLE = "products";
        public static final String SELECT_BY_PRODUCTID = "SELECT * FROM " + TABLE + " WHERE productId = #{productId}";
        public static final String UPDATE_STATUS = "UPDATE " + TABLE + " SET status = #{status} WHERE productId = #{productId}";
        public static final String DELETE = "DELETE FROM " + TABLE + " WHERE productId = #{productId} AND status = #{status}";
        public static final String SELECT_BY_OUTERID = "SELECT * FROM " + TABLE + " WHERE outerId = #{outerId}";

        public static String insert(Product product) {
            SQL sql = new SQL().INSERT_INTO(TABLE)
                    .VALUES("productName", "#{productName}")
                    .VALUES("feeType", "#{feeType}")
                    .VALUES("standardPrice", "#{standardPrice}")
                    .VALUES("realPrice", "#{realPrice}")
                    .VALUES("denominationprice", "#{denominationprice}")
                    .VALUES("createTime", "#{createTime}")
                    .VALUES("status", "#{status}");

            if (product.getOuterId() != null) {
                sql.VALUES("outerId", "#{outerId}");
            }

            if (product.getProviderId() != 0) {
                sql.VALUES("providerId", "#{providerId}");
            }

            return sql.toString();
        }

        public String update(Product product) {
            SQL sql = new SQL().UPDATE(TABLE);
            if (product.getProductName() != null) {
                sql.SET("productName=#{productName}");
            }

            if (product.getStandardPrice() > 0) {
                sql.SET("standardPrice=#{standardPrice}");
            }

            if (product.getRealPrice() > 0) {
                sql.SET("realPrice=#{realPrice}");
            }

            if (product.getDenominationprice() > 0) {
                sql.SET("denominationprice=#{denominationprice}");
            }

            if (product.getStatus() != null) {
                sql.SET("status=#{status}");
            }

            sql.WHERE("productId = #{productId}").AND().WHERE("outerId = #{outerId}");
            return sql.toString();
        }

    }

    @Select(Sql.SELECT_BY_PRODUCTID)
    Product findByProductId(long productId);

    @Select(Sql.SELECT_BY_OUTERID)
    Product findByOuterId(String outerId);

    @InsertProvider(type = Sql.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    int add(Product product);

    @SelectProvider(type = Paging.class, method = "list")
    List<Product> find(Paging paging);

    @SelectProvider(type = Paging.class, method = "first")
    List<Long> first(Paging paging);

    @SelectProvider(type = Paging.class, method = "forward")
    List<Long> forward(Paging paging);

    @SelectProvider(type = Paging.class, method = "backward")
    List<Long> backward(Paging paging);

    @UpdateProvider(type = Sql.class, method = "update")
    int update(Product product);

    @Update(Sql.UPDATE_STATUS)
    int updateStatus(@Param("productId") long productId, @Param("status") Product.Status status);

    @Delete(Sql.DELETE)
    int delete(@Param("productId") long productId, @Param("status") Product.Status status);
}
