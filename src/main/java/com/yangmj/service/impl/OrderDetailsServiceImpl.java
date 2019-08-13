package com.yangmj.service.impl;

import com.github.pagehelper.PageHelper;
import com.yangmj.common.MyPageInfo;
import com.yangmj.common.SystemDefault;
import com.yangmj.entity.OrderDetails;
import com.yangmj.entity.OrderItem;
import com.yangmj.mapper.OrderDetailsMapper;
import com.yangmj.mapper.OrderItemMapper;
import com.yangmj.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Override
    public MyPageInfo<OrderDetails> queryOrderDetailsAll(OrderDetails orderDetails) {
        PageHelper.startPage(orderDetails.getPageNum(),orderDetails.getPageSize());
        List<OrderDetails> orderDetailsList = orderDetailsMapper.queryOrderDetailsAll(orderDetails);
        MyPageInfo<OrderDetails> orderDetailsPageInfo = new MyPageInfo<>(orderDetailsList);
        return orderDetailsPageInfo;
    }

    @Override
    @Transactional
    public String insertOrderDetails(OrderDetails orderDetails) {

        //入库前判断当前的人数是达到规定的总人数
        String result = "00";
        List<Map> partInAndAllNumMap = orderDetailsMapper.judgeStaffFull(orderDetails.getOrderId());
        Map mapNum = partInAndAllNumMap.get(0);
//        isFull 0满员 1 未满员
        String isFull = (String)mapNum.get("isFull");
        if("1".equals(isFull) || StringUtils.isEmpty(isFull)){
            //参与人数 partInNum
            int partInNum = ((Number)mapNum.get("partInNum")).intValue();
//        Number numCount = (Number)hashMap.get(orderItemquery.getId());
            //项目总人数 totalNum
            int totalNum = ((Number)mapNum.get("totalNum")).intValue();
            int diValue = totalNum - partInNum ;
            if(diValue > 0){
                int resultOrder = orderDetailsMapper.insertOrderDetails(orderDetails);
                //活动项目最后一个人满的时候，需要将人员设置为满员
                if(1 ==  diValue && 1 == resultOrder){

                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(orderDetails.getOrderId());
                    List<OrderItem> orderItemList = orderItemMapper.queryOrderItemAll(orderItem);
                    if (!CollectionUtils.isEmpty(orderItemList)) {
                        OrderItem queryItem = orderItemList.get(0);
                        //                0满员 1 未满员
                        queryItem.setIsFull("0");
                        //                0 组队中 1 待参加 2 已结束
                        queryItem.setOrderStatus("1");
                        //                0可以开始游戏，1还在招募人员
                        queryItem.setGameStatus("0");
                        orderItemMapper.updateOrderItemByKey(queryItem);
                        //TODO 最后一个人拼团成功后，返回服务通知消息，该项目的信息
//                        result ="notice_ok";
                    }

                }
                return result;
            }else {
                return result = "01";
            }
        }else {
            return "该项目人数满了，请看看其他活动哟~";
        }

    }

    /**
     * 查询是否重复参加该订单
     * @param orderId
     * @param wechatOpenid
     * @return
     */
    @Override
    public int verifyRepeatedPartIn(Integer orderId, String wechatOpenid) {
        return orderDetailsMapper.verifyRepeatedPartIn(orderId, wechatOpenid);
    }

    /**
     * 查看详情
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> viewDetailsOneOrder(Integer id) {
        List<Map> mapList = orderDetailsMapper.viewDetailsOneOrder(id);

        Map<String, Object> hashMap = new HashMap<>();
        ArrayList<Object> peopleInfo = new ArrayList<>();


        if (!CollectionUtils.isEmpty(mapList)) {
            hashMap = mapList.get(0);
           //人的联系方式做为一个字段的list集合传输出去
            String captainOpenId = null;
            for (int i = 0; i <mapList.size() ; i++) {
                HashMap<Object, Object> infoMap = new HashMap<>();
                Map queryMap = mapList.get(i);
                String avatarUrl = (String)queryMap.get("avatarUrl");
                String phone = (String) queryMap.get("phone");
                String nickName = (String)queryMap.get("nickName");
                String openId = (String)queryMap.get("openId");
                //是否为队长
                String isCaptain = (String) queryMap.get("isCaptain");

                //如果是队长，将队长的openid单独发出去
                if ("0".equals(isCaptain)) {
                    captainOpenId = openId;
                }
                infoMap.put("avatarUrl", avatarUrl);
                infoMap.put("phone",phone );
                infoMap.put("nickName",nickName );
                infoMap.put("openId",openId );
                //0是 1否
                infoMap.put("isCaptain",isCaptain );
                peopleInfo.add(infoMap);
            }
            //关于金额的信息返回是否AA 还是免费
            String feeTags = (String)hashMap.get("feeTags");
            if ("AA".equals(feeTags)) {
                Object perCost = hashMap.get("perCost");
                hashMap.put("perCost", perCost + "/人");
            } else {
                hashMap.put("perCost", SystemDefault.PAY_OO);
            }
            //页面不展示这些字段
            hashMap.remove("phone");
            hashMap.remove("avatarUrl");
            hashMap.remove("nickName");
            hashMap.remove("openId");
            hashMap.remove("isCaptain");
//            hashMap.remove("totalNum");
            //页面新增字段 当前人数
            hashMap.put("currNum", mapList.size());
            //将队长的openid单独发出去，作为页面判断
            hashMap.put("captainOpenId", captainOpenId);
            //判断当前队伍的是否满员了
            hashMap.put("peopleInfo", peopleInfo);
        }

        return hashMap;
    }
}
