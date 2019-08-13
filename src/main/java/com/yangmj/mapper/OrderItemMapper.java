package com.yangmj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.yangmj.entity.OrderItem;

import java.util.HashMap;
import java.util.List;
@Mapper
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insertOrderItem(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> queryOrderItemAll(OrderItem orderItem);

    int updateOrderItemByKeySelective(OrderItem record);

    /**
     * 根据主键更新订单的详情信息
     * @param record
     * @return
     */
    int updateOrderItemByKey(OrderItem record);

//    @Select("select *,count(*) as count from message WHERE toid = #{userId} GROUP BY formid ORDER BY created_date desc limit #{offset}, #{limit}")
//    List<Message> selectConversationList(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select("select item.id,item.sport_title,item.curr_Num,item.total_Num,item.acture_start_tm,item.game_location,project.first_page_url sport_img_url,item.end_price endPrice ,item.fee_tags feeTags from order_item item ,order_details detail,project_item project where item.id = detail.order_id and detail.is_captain =#{isCaptain}" +
            " and item.order_status in (#{orderStatus}) and detail.wechat_openid =#{wechatOpenid} and detail.is_active ='0' and project.project_id = item.project_id order by item.create_time desc")
    List<OrderItem> queryLeaderOrFollower(@Param("isCaptain") String isCaptain, @Param("orderStatus") String orderStatus, @Param("wechatOpenid") String wechatOpenid);

    /**
     * 订单的取消
     */

    @Update("update order_item item set order_status ='3' where id = #{id}")
    int cancleOrderByKey(@Param("id") Integer isCaptain);

    /**
     * 定时任务启动，关闭时间到期还没有满员的订单
     * @return
     */
    @Select("select * from order_item item where item.is_full ='1' and item.order_status ='0' and UNIX_TIMESTAMP(item.end_time)<UNIX_TIMESTAMP(now())")
    List<OrderItem> timerCloseOrder();

    int updateOrderItemBatch(HashMap hashMap);

    /**
     * 订单时间到了最后的时间，该订单进行关闭,正常游戏结束的订单
     */
    @Select("select * from order_item item where item.is_full in('1','0') and item.order_status ='1' and UNIX_TIMESTAMP(item.end_time)<UNIX_TIMESTAMP(now())")
    List<OrderItem> timerCloseOrderNormalEnd();
}