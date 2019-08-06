package xin.yangmj.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xin.yangmj.common.MyPageInfo;
import xin.yangmj.common.ResponseResult;
import xin.yangmj.entity.OrderDetails;
import xin.yangmj.entity.OrderItem;
import xin.yangmj.service.OrderDetailsService;
import xin.yangmj.service.OrderItemService;
import xin.yangmj.util.DateUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    /**
     * 有条件查询订单
     * 查询所有的订单
     * @param orderItem
     * @return
     */
    @PostMapping("/findAllOrders")
    public ResponseResult queryOrderItemAll(@RequestBody OrderItem orderItem){
        /*
            01 5人以下
            02 5-8人
            03 8-10人
            04 10人以上
         */

        /*if ("01".equals(num)) {
            orderItem.setTotalNum(1);
            orderItem.setTotalNumUp(5);
        }*/
        //根据人数筛选
        String numType = orderItem.getNumType();
        String costRMB = orderItem.getCostRMB();
        String queryDate = orderItem.getQueryDate();
        if(!StringUtils.isEmpty(numType)){
            Integer num = Integer.valueOf(numType);
            switch (num) {
                case 1:
                    orderItem.setTotalNum(1);
                    orderItem.setTotalNumUp(5);
                    break;
                case 2:
                    orderItem.setTotalNum(5);
                    orderItem.setTotalNumUp(8);
                    break;
                case 3:
                    orderItem.setTotalNum(8);
                    orderItem.setTotalNumUp(10);
                    break;
                default:
                    orderItem.setTotalNum(10);
                    break;
//                orderItem.setTotalNumUp(5);
            }
        }
        /*
            根据订单总金额筛选
            01 免费
            02 人均50以内
            03 人均50-100
            04 人均100以上
         */
        if(!StringUtils.isEmpty(costRMB)){
            if ("01".equals(costRMB)) {
//                OO 免费 AA
                orderItem.setFeeTags("AA");
            }else if ("02".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(0));
                orderItem.setEndPrice(new BigDecimal(50));
            }else if ("03".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(50));
                orderItem.setEndPrice(new BigDecimal(100));
            }else {
                orderItem.setProjectCost(new BigDecimal(100));
            }
        }
        //根据日期筛选
        if (!StringUtils.isEmpty(queryDate)) {
//            yyyy/mm/dd to yyyy-mm-dd
            String yyyyToYYYY = DateUtil.yyyyToYYYY(queryDate);
            orderItem.setActureStartTm(yyyyToYYYY);
        }
        MyPageInfo<OrderItem> projectItemPageInfo = orderItemService.queryOrderItemAll(orderItem);
        ResponseResult resp = ResponseResult.makeSuccResponse(null, projectItemPageInfo);
        return resp;
    }

    /**
     * 查看我发起运动项目和我参加的运动项目
     * 运动的最新状态
     */
    @PostMapping("/queryLeaderOrFollower")
    public ResponseResult queryLeaderOrFollower( @RequestBody JSONObject jsonObject){
        String isCaptain=jsonObject.get("isCaptain").toString();
        String orderStatus=jsonObject.get("orderStatus").toString();
        String wechatOpenid=jsonObject.get("wechatOpenid").toString();
        List<OrderItem> orderItems = orderItemService.queryLeaderOrFollower(isCaptain, orderStatus,wechatOpenid);
        ResponseResult resp = ResponseResult.makeSuccResponse(null, orderItems);
        return resp;
    }


    /**
     * 创建订单的信息
     * @param orderItem
     * @return
     */
    @PostMapping("/createOrderItem")
    @Transactional
    public ResponseResult createProjectItem(@RequestBody OrderItem orderItem) {

        OrderDetails orderDetails = new OrderDetails();
        ResponseResult resp = null;
        try {
//            发起订单的人初始为1
            orderItem.setCurrNum(1);
//            0开始游戏，1还在招募人员，主要是针对手动关闭订单开始游戏
            orderItem.setGameStatus("1");
            Integer totalNum = orderItem.getTotalNum();
            if(1 == totalNum){
//                订单只有一个人的情况
                orderItem.setOrderStatus("1");
                orderItem.setIsFull("0");
            }else {
                //            订单状态，0 组队中 1 待参加 2 已结束 3取消
                orderItem.setOrderStatus("0");
                //            0满员 1 未满员
                orderItem.setIsFull("1");
            }
            int item = orderItemService.insertOrderItem(orderItem);
            //订单创建的时候，需要将发起人的信息插入到order_details订单明细表
            orderDetails.setOrderId(orderItem.getId());
            orderDetails.setWechatOpenid(orderItem.getStartWechatOpenid());
            // 0 在线  1 退出拼单
            orderDetails.setIsActive("0");
            orderDetails.setProjectFee(orderItem.getProjectCost());
            //0是 1否 发起人
            orderDetails.setIsCaptain("0");
//            int i = 1/0;
            String details = orderDetailsService.insertOrderDetails(orderDetails);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(null, "服务器繁忙");
        }
        return resp;
    }


    /**
     * 订单人数未达标，人为关闭订单，意义是即使人员没有满也可以开始游戏
     * @param orderItem
     * @return
     */
    @PostMapping("/tmpStartGame")
    public ResponseResult tmpStartGame(@RequestBody OrderItem orderItem) {
//        安全性需要自己从表或者然后更新
        MyPageInfo<OrderItem> queryOrderItemAll = orderItemService.queryOrderItemAll(orderItem);
        OrderItem orderItemBean = queryOrderItemAll.getList().get(0);
//        0满员 1 未满员
        orderItemBean.setIsFull("1");
//        0 组队中 1 待参加 2 已结束
        orderItemBean.setOrderStatus("1");
//        按钮显示是否可以玩，不管人数满员否，0开始游戏，1还在招募人员
        orderItemBean.setGameStatus("0");
        orderItem.setUpdateTime(DateUtil.formatDateTime());
        ResponseResult resp = null;
        try {
            int itemResult = orderItemService.updateOrderItemByKey(orderItemBean);
            resp = ResponseResult.makeSuccResponse(null, itemResult);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse("网络错误", "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    /**
     * 发起人取消订单
     * @param orderItem
     * @return
     */
    @PostMapping("/cancleOrder")
    public ResponseResult cancleOrder(@RequestBody OrderItem orderItem) {

        //        0 组队中 1 待参加 2 已结束 3 取消
        orderItem.setOrderStatus("3");
        orderItem.setUpdateTime(DateUtil.formatDateTime());

        ResponseResult resp = null;
        try {
            int item = orderItemService.updateOrderItemByKeySelective(orderItem);
            resp = ResponseResult.makeSuccResponse(null, item);
        } catch (Exception e) {
            e.printStackTrace();
            resp = ResponseResult.makeFailResponse("网络错误", "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    /**
     * 定时任务启动，关闭时间到期还没有满员的订单
     * @return
     */
    @PostMapping("/timerCloseOrder")
    public void timerCloseOrder() {

        List<OrderItem> orderItemList = orderItemService.timerCloseOrder();
        HashMap<Object, Object> hashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderItemList)) {
            /*for (int i = 0; i < orderItemList.size(); i++) {
                OrderItem queryOrderItem = orderItemList.get(i);
//                0 组队中 1 待参加 2 已结束 3取消
                queryOrderItem.setOrderStatus("3");
            }*/
            hashMap.put("ids",orderItemList);
            //批量更新数据的信息
            int i = orderItemService.updateOrderItemBatch(hashMap);
            if (i > 0) {
                log.info("批量更新:{}",i);
            }
        }
    }

}
