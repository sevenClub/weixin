package xin.yangmj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xin.yangmj.entity.OrderDetails;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insertOrderDetails(OrderDetails record);

    int insertSelective(OrderDetails record);

    List<OrderDetails> queryOrderDetailsAll(OrderDetails orderDetails);

    int updateByPrimaryKeySelective(OrderDetails record);

    int updateByPrimaryKey(OrderDetails record);

    @Select("select t.order_id orderId,count(1) countNumFore from order_details t,order_item item  where t.is_active = '0' and item.id =t.order_id and item.order_status <> '2' group by t.order_id")
//    List<Object> selectConversationList(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);
    List<Map> selectConversationList();

}