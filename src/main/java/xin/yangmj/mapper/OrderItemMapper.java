package xin.yangmj.mapper;

import org.apache.ibatis.annotations.Mapper;
import xin.yangmj.entity.OrderItem;

import java.util.List;
@Mapper
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insertOrderItem(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> queryOrderItemAll(OrderItem orderItem);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}