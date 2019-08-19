package com.yangmj.controller;

import com.alibaba.fastjson.JSONObject;
import com.yangmj.common.MyPageInfo;
import com.yangmj.common.ResponseResult;
import com.yangmj.common.SystemDefault;
import com.yangmj.entity.OrderDetails;
import com.yangmj.entity.OrderItem;
import com.yangmj.service.*;
import com.yangmj.util.CommonUtils;
import com.yangmj.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

@RestController
@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private ProjectItemService projectItemService;
    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private WechatService wechatService;

    //详情页面的图片的信息
    @Value("${login.url}")
    private  String loginUrl;

    @Value("${wechat.templateId.startOrder}")
    private String templateIdStartOrder;


    /**
     * 有条件查询订单
     * 查询所有的订单
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/findAllOrders")
    public ResponseResult queryOrderItemAll(@RequestBody OrderItem orderItem) {
        //获取组合的条件筛选，默认全部传空
        HashMap sort = orderItem.getSort();
        //运动类型
        String sportType = (String)sort.get("sportType");
        //人数范围
        String numType = (String)sort.get("numType");
        //金额范围
        String costRMB = (String)sort.get("costRMB");
        //时间范围
        String queryDate = (String)sort.get("queryDate");
        //根据类型筛选
        if (StringUtils.isEmpty(sportType)) {
            orderItem.setProjectId("");
        }else {
            orderItem.setProjectId(sportType);
        }
         /*根据人数筛选
            01 5人以下
            02 5-8人
            03 8-10人
            04 10人以上
            all 所有人,传递空参数
         */
        if (!StringUtils.isEmpty(numType)) {
            if ("01".equals(numType)) {
                orderItem.setTotalNum(0);
                orderItem.setTotalNumUp(5);
            }else if("02".equals(numType)) {
                orderItem.setTotalNum(5);
                orderItem.setTotalNumUp(8);
            }else if("03".equals(numType)) {
                orderItem.setTotalNum(8);
                orderItem.setTotalNumUp(10);
            }else{
                orderItem.setTotalNum(10);
            }
        }else {

        }
        /*
            根据订单总金额筛选
            01 免费
            02 人均50以内
            03 人均50-100
            04 人均100以上
            all 所有
         */
        if (!StringUtils.isEmpty(costRMB)) {
            if ("01".equals(costRMB)) {
//                OO: 免费 AA:AA制
                orderItem.setFeeTags("OO");
            } else if ("02".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(0));
                orderItem.setEndPrice(new BigDecimal(50));
                orderItem.setFeeTags("AA");
            } else if ("03".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(50));
                orderItem.setEndPrice(new BigDecimal(100));
            } else if ("04".equals(costRMB)) {
                orderItem.setProjectCost(new BigDecimal(100));
            }
        }else {
            //查询所有的
        }
        //根据时间筛选 01 今天，02 明天 03 本周 04 本月
        if (!StringUtils.isEmpty(queryDate)) {
            String today = DateUtil.formatDate();
            if ("01".equals(queryDate)) {
                orderItem.setActureStartTm(today);
            }else if ("02".equals(queryDate)) {
                String tomorrow = DateUtil.addDays(today, 1);
                orderItem.setActureStartTm(tomorrow);
            }else if ("03".equals(queryDate)) {
                Date endDayOfWeek = DateUtil.getEndDayOfWeek();
                orderItem.setTmpDate(today);
                orderItem.setEndTime(endDayOfWeek.toString());
            }else{
                String maxMonthDate = DateUtil.getMaxMonthDate();
                orderItem.setTmpDate(today);
                orderItem.setEndTime(maxMonthDate);
            }

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
    public ResponseResult queryLeaderOrFollower(@RequestBody JSONObject jsonObject) {
        ResponseResult resp = null;
        if (jsonObject.size() == 0) {
            log.info("参数缺失，请稍后再试");
            return resp = ResponseResult.makeFailResponse(SystemDefault.SERVER_ERROR_500, "");
        }
        //isCaptain 0我发起的 1 我参加的
        //orderStatus 0 组队中 1 待参加 2 已结束
        String isCaptain = jsonObject.get("isCaptain").toString();
        String orderStatus = jsonObject.get("orderStatus").toString();
        String wechatOpenid = jsonObject.get("wechatOpenid").toString();
        Integer pageSize =(Integer) jsonObject.get("pageSize");
        Integer pageNum = (Integer)jsonObject.get("pageNum");
        //查看已经结束的的，包含2： 正常结束 3：时间到组队失败 4：发起者人为取消订单
        ArrayList<Object> list = new ArrayList<>();
        HashMap<Object, Object> hashMap = new HashMap<>();
        if ("0".equals(orderStatus)) {
            list.add("0");
        }
        if ("1".equals(orderStatus)) {
            list.add("1");
        }
        if ("2".equals(orderStatus)) {
            list.add("2");
            list.add("3");
            list.add("4");
        }
        hashMap.put("isCaptain", isCaptain);
        hashMap.put("orderStatus", list);
        hashMap.put("wechatOpenid", wechatOpenid);
        hashMap.put("pageNum", pageNum);
        hashMap.put("pageSize", pageSize);

        try {
            MyPageInfo<OrderItem> orderItemMyPageInfo = orderItemService.queryLeaderOrFollower(hashMap);
//            List<OrderItem> orderItems = orderItemService.queryLeaderOrFollower(isCaptain, orderStatus, wechatOpenid);
            resp = ResponseResult.makeSuccResponse(null, orderItemMyPageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询失败queryLeaderOrFollower，原因"+e.toString());
            resp = ResponseResult.makeFailResponse(SystemDefault.SERVER_ERROR_500, "");
        }
        return resp;
    }


    /**
     * 创建订单的信息
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/createOrderItem")
    @Transactional
    public ResponseResult createProjectItem(@RequestBody OrderItem orderItem) {
        String openid = orderItem.getStartWechatOpenid();
        String formId = orderItem.getFormId();
        ResponseResult resp = null;
        //验证手机号是否输入或输入是否合法
        String phone = orderItem.getContactDir();
        String phoneResult = CommonUtils.checkPhone(phone);
        if ("01".equals(phoneResult)) {
            return ResponseResult.makeFailResponse(SystemDefault.MOBILE_MISSING, "");
        }
        if ("02".equals(phoneResult)){
                return ResponseResult.makeFailResponse(SystemDefault.MOBILE_ERROR, "");
        }

        OrderDetails orderDetails = new OrderDetails();

        try {
            //发起订单的人初始为1
            orderItem.setCurrNum(1);
            //0开始游戏，1还在招募人员，主要是针对手动关闭订单开始游戏
            orderItem.setGameStatus("1");
            Integer totalNum = orderItem.getTotalNum();
            //订单只有一个人的情况
            if (1 == totalNum) {
                orderItem.setOrderStatus("1");
                orderItem.setIsFull("0");
            } else {
                //0 组队中 1 待参加 2： 正常结束 3：时间到组队失败 4：发起者人为取消订单
                orderItem.setOrderStatus("0");
                //0满员 1 未满员
                orderItem.setIsFull("1");
            }
            //TODO 创建订单的时候需要判断当前时间点是否冲突,待讨论定义
            //创建订单如果是AA的，设置每个人的花费
            String feeTags = orderItem.getFeeTags();
            if ("AA".equals(feeTags)) {
                System.out.println("totalNum***"+totalNum);
                BigDecimal projectCost = orderItem.getProjectCost();
                System.out.println("projectCost***"+projectCost);
                BigDecimal perCost = projectCost.divide(new BigDecimal(totalNum),2,ROUND_HALF_DOWN);
                orderItem.setEndPrice(perCost);
            } else {
                orderItem.setEndPrice(new BigDecimal(0));
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
            //手机号
            orderDetails.setContactDir(phone);
            String details = orderDetailsService.insertOrderDetails(orderDetails);
            resp = ResponseResult.makeSuccResponse(null, item);

            //消息推送
            String access_token = wechatService.loginWechatNotice();
            String time = orderItem.getActureStartTm()+"~"+ orderItem.getEndTime();
            String[] value = {orderItem.getSportTitle(),time,orderItem.getGameLocation(),"5ren","xxx"};
            messagePushService.pushOneUser(access_token,openid,formId,templateIdStartOrder,value);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resp = ResponseResult.makeFailResponse(SystemDefault.SERVER_ERROR_500, "");
        }
        return resp;
    }


    /**
     * 订单人数未达标，人为关闭订单，即使人员没有满也可以开始游戏
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/tmpStartGame")
    public ResponseResult tmpStartGame(@RequestBody OrderItem orderItem) {
//        安全需要自己从表或者然后更新
//        MyPageInfo<OrderItem> queryOrderItemAll = orderItemService.queryOrderItemAll(orderItem);
        OrderItem orderItemBean = orderItemService.queryOrderItemByKey(orderItem);
//        OrderItem orderItemBean = queryOrderItemAll.getList().get(0);
//        0满员 1 未满员
        orderItemBean.setIsFull("0");
//        0 组队中 1 待参加 2： 正常结束 3：时间到组队失败 4：发起者人为取消订单）
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
            resp = ResponseResult.makeFailResponse(SystemDefault.NETWORK_ERROR, "");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resp;
    }

    /**
     * 发起人取消订单
     *
     * @param orderItem
     * @return
     */
    @PostMapping("/cancleOrder")
    public ResponseResult cancleOrder(@RequestBody OrderItem orderItem) {

        //        0 组队中 1 待参加 2： 正常结束 3：时间到组队失败 4：发起者人为取消订单）
        orderItem.setOrderStatus("4");
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
     *
     * @return
     */
    //@PostMapping("/timerCloseOrder") //页面测试
    //定时任务时间的
    @Scheduled(cron = "*/30 * * * * ?")
    public void timerCloseOrderNoFull() {
        List<OrderItem> orderItemList = orderItemService.timerCloseOrder();
        HashMap<Object, Object> hashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderItemList)) {
            hashMap.put("ids", orderItemList);
            //3 为人数未满订单关闭
            hashMap.put("status", "3");
            //批量更新数据的信息
            int i = orderItemService.updateOrderItemBatch(hashMap);
            if (i > 0) {
                log.info("批量更新关闭时间到期还没有满员的订单:{}", i);
            }
        }
    }

    /**
     * 订单时间到了最后的时间，该订单进行关闭
     */
//    @PostMapping("/timerCloseOrderNormalEnd")
    @Scheduled(cron = "*/30 * * * * ?")
    public void timerCloseOrderNormalEnd() {
        List<OrderItem> orderItemList = orderItemService.timerCloseOrderNormalEnd();
        HashMap<Object, Object> hashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderItemList)) {
            hashMap.put("ids", orderItemList);
            //2为订单正常关闭
            hashMap.put("status", "2");
            //批量更新数据的信息
            int i = orderItemService.updateOrderItemBatch(hashMap);
            if (i > 0) {
                log.info("批量更新关闭时间到期正常结束的订单数量:{}", i);
            }
        }

    }

}
