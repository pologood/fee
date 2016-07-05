package com.sogou.pay.fee.mapper;


import com.sogou.pay.fee.entity.Order;
import commons.mybatis.Paging;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public interface OrderMapper {
    class Sql {
        public static final String TABLE = "orders";
        public static final String SELECT_BY_ORDERID = "SELECT * FROM " + TABLE + " WHERE orderId = #{orderId}";
        public static final String DELETE = "DELETE FROM " + TABLE + " WHERE orderId = #{orderId} AND status = #{status}";
        public static final String UPDATE_STATUS = "UPDATE " + TABLE + " SET status = #{status} WHERE orderId = #{orderId}";
        public static final String COUNT_BY_PHONE = "SELECT COUNT(*) FROM " + TABLE + " WHERE phone = #{phone}";
        public static final String COUNT_BY_USER = "SELECT COUNT(*) FROM " + TABLE + " WHERE userId = #{userId}";

        public static String insert(Order order) {
            SQL sql = new SQL().INSERT_INTO(TABLE)
                    .VALUES("feeType", "#{feeType}")
                    .VALUES("productId", "#{productId}")
                    .VALUES("totalAmount", "#{totalAmount}")
                    .VALUES("curPrice", "#{curPrice}")
                    .VALUES("quantity", "#{quantity}")
                    .VALUES("payChanel", "#{payChanel}")
                    .VALUES("status", "#{status}")
                    .VALUES("createTime", "#{createTime}");

            if (order.getUserId() != null) {
                sql.VALUES("userId", "#{userId}");
            }

            if (order.getPhone() != null) {
                sql.VALUES("phone", "#{phone}");
            }

            if (order.getSpecifiedNo() != null) {
                sql.VALUES("specifiedNo", "#{specifiedNo}");
            }

            if (order.getOuterId() != null) {
                sql.VALUES("outerId", "#{outerId}");
            }


            return sql.toString();
        }

    }

    @Select(Sql.SELECT_BY_ORDERID)
    Order findByOrderId(long orderId);

    @InsertProvider(type = Sql.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    int add(Order order);

    @SelectProvider(type = Paging.class, method = "list")
    List<Order> find(Paging paging);

    @SelectProvider(type = Paging.class, method = "first")
    List<Long> first(Paging paging);

    @SelectProvider(type = Paging.class, method = "forward")
    List<Long> forward(Paging paging);

    @SelectProvider(type = Paging.class, method = "backward")
    List<Long> backward(Paging paging);

    @Delete(Sql.DELETE)
    int delete(@Param("orderId") long orderId, @Param("status") Order.Status status);

    @Update(Sql.UPDATE_STATUS)
    int updateStatus(@Param("orderId") long orderId, @Param("status") Order.Status status);

    @Select(Sql.COUNT_BY_PHONE)
    int countByPhone(String phone);

    @Select(Sql.COUNT_BY_USER)
    int countByUser(String userId);


}
