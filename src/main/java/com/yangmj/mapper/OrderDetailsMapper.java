package com.yangmj.mapper;

import com.yangmj.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.yangmj.entity.OrderDetails;

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

    /**
     * 查询所有的订单，在活动中的，不包含已经结束的订单
     * @return
     */
    @Select("select t.order_id orderId,count(1) countNumFore from order_details t,order_item item  where t.is_active = '0' and item.id =t.order_id and item.order_status not in ('2','3','4') group by t.order_id")
//    List<Object> selectConversationList(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);
    List<Map> selectConversationList();


    /**
     * 拼单前判断该项目目前人数是否超了，需要判断后插入数据
     * 该订单还在线的人，且该订单还是激活状态
     *
     */
    @Select("select count(1) partInNum, item.total_Num totalNum,is_full isFull from order_item item ,order_details detail where detail.order_id = item.id and detail.order_id = #{orderId} and detail.is_active ='0' and item.order_status ='0'")
    List<Map> judgeStaffFull(@Param("orderId") Integer orderId);

    /**
     * 查询是否重复参加该订单
     * @param orderId
     * @param wechatOpenid
     * @return
     */
    @Select("select count(1) countNum from order_details detail where detail.order_id = #{orderId} and detail.wechat_openid =#{wechatOpenid} and detail.is_active ='0'")
    int verifyRepeatedPartIn(@Param("orderId") Integer orderId, @Param("wechatOpenid") String wechatOpenid);

    /**
     * 查看详情
     * @param id
     * @return
     */
    @Select("SELECT item.id,item.sport_title sportTitle,item.fee_tags feeTags,item.acture_start_tm actureStartTm,item.game_location gameLocation,cu.avatar_url avatarUrl,item.description,cu.phone,item.sport_img_url sportImgUrl from order_item item ,order_details detail,consumer cu where item.id = detail.order_id and detail.wechat_openid = cu.wechat_openid and item.id =#{id}")
    List<Map> viewDetailsOneOrder(@Param("id") Integer id);

}