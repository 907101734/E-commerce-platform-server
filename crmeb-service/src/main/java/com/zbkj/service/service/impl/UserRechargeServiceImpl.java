package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.PayConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.finance.UserRecharge;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserBill;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRechargeConfirmRequest;
import com.zbkj.common.request.UserRechargeReviewRequest;
import com.zbkj.common.request.UserRechargeSaveRequest;
import com.zbkj.common.request.UserRechargeSearchRequest;
import com.zbkj.common.response.UserRechargeResponse;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.vo.DateLimitUtilVo;
import com.zbkj.service.dao.UserRechargeDao;
import com.zbkj.service.service.UserBillService;
import com.zbkj.service.service.UserRechargeService;
import com.zbkj.service.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserRechargeServiceImpl 接口实现
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2022 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 */
@Service
public class UserRechargeServiceImpl extends ServiceImpl<UserRechargeDao, UserRecharge> implements UserRechargeService {

    @Resource
    private UserRechargeDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 列表
     * @param request          请求参数
     * @param pageParamRequest 分页类参数
     * @return List<UserRecharge>
     */
    @Override
    public PageInfo<UserRechargeResponse> getList(UserRechargeSearchRequest request, PageParamRequest pageParamRequest, String right) {
        Page<UserRecharge> userRechargesList = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        DateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
        //带 UserExtract 类的多条件查询
        LambdaQueryWrapper<UserRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getUid()) && request.getUid() > 0) {
            lambdaQueryWrapper.eq(UserRecharge::getUid, request.getUid());
        }
        if (StrUtil.isNotBlank(request.getKeywords())) {
            lambdaQueryWrapper.like(UserRecharge::getOrderId, request.getKeywords()); //订单号
        }
        if (request.getStatus() != null) {
            lambdaQueryWrapper.eq(UserRecharge::getStatus, request.getStatus());
        }
        //是否充值
        // lambdaQueryWrapper.eq(UserRecharge::getPaid, true);

        //时间范围
        if (StrUtil.isNotBlank(dateLimit.getStartTime()) && StrUtil.isNotBlank(dateLimit.getEndTime())) {
            //判断时间
            int compareDateResult = DateUtil.compareDate(dateLimit.getEndTime(), dateLimit.getStartTime(), Constants.DATE_FORMAT);
            if (compareDateResult == -1) {
                throw new CrmebException("开始时间不能大于结束时间！");
            }

            lambdaQueryWrapper.between(UserRecharge::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }

        lambdaQueryWrapper.orderByDesc(UserRecharge::getId);

        List<UserRecharge> userRecharges = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(userRecharges)) {
            return CommonPage.copyPageInfo(userRechargesList, CollUtil.newArrayList());
        }

        List<Integer> userIds = userRecharges.stream().map(UserRecharge::getUid).collect(Collectors.toList());
        HashMap<Integer, User> userHashMap = userService.getMapListInUid(userIds);
        List<UserRechargeResponse> responseList = userRecharges.stream().map(e -> {
            User user = userHashMap.get(e.getUid());
            UserRechargeResponse r = new UserRechargeResponse();
            BeanUtils.copyProperties(e, r);
            if (null != user) {
                r.setAvatar(user.getAvatar());
                r.setNickname(user.getNickname());
                r.setPhone(CrmebUtil.maskMobile(user.getPhone()));
            }
            return r;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(userRechargesList, responseList);
    }

    /**
     * 充值总金额
     * @return HashMap<String, BigDecimal>
     */
    @Override
    public HashMap<String, BigDecimal> getBalanceList() {
        HashMap<String, BigDecimal> map = new HashMap<>();

        //后台充值
        BigDecimal systemPay = dao.getSumByType("systemPay");
        if (null == systemPay) {
            systemPay = BigDecimal.ZERO;
        }
        map.put("systemPay", systemPay);

        BigDecimal routine = dao.getSumByType("routine");
        if (null == routine) {
            routine = BigDecimal.ZERO;
        }
        map.put("routine", routine); //小程序充值

        //        BigDecimal weChat = dao.getSumByType("weixin");
        BigDecimal weChat = dao.getSumByType("public");
        if (null == weChat) {
            weChat = BigDecimal.ZERO;
        }
        map.put("weChat", weChat); //公众号充值

        BigDecimal total = dao.getSumByType("");
        if (null == total) {
            total = BigDecimal.ZERO;
        }
        map.put("total", total); //总金额

        BigDecimal refund = dao.getSumByRefund();
        if (null == refund) {
            refund = BigDecimal.ZERO;
        }
        map.put("refund", refund);

        map.put("other", total.subtract(routine).subtract(weChat)); //其他金额

        return map;
    }

    /**
     * 根据对象查询订单
     * @return UserRecharge
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    @Override
    public UserRecharge getInfoByEntity(UserRecharge userRecharge) {
        LambdaQueryWrapper<UserRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.setEntity(userRecharge);
        return dao.selectOne(lambdaQueryWrapper);
    }

    /**
     * 根据日期获取充值订单数量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    @Override
    public Integer getRechargeOrderNumByDate(String date) {
        QueryWrapper<UserRecharge> wrapper = Wrappers.query();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(wrapper);
    }

    /**
     * 根据日期获取充值订单金额
     * @param date 日期，yyyy-MM-dd格式
     * @return BigDecimal
     */
    @Override
    public BigDecimal getRechargeOrderAmountByDate(String date) {
        QueryWrapper<UserRecharge> wrapper = Wrappers.query();
        wrapper.select("IFNULL(sum(price), 0) as price");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return dao.selectOne(wrapper).getPrice();
    }

    /**
     * 获取总人数
     * @return Integer
     */
    @Override
    public Integer getTotalPeople() {
        QueryWrapper<UserRecharge> wrapper = Wrappers.query();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.groupBy("uid");
        List<UserRecharge> list = dao.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    /**
     * 获取总金额
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalPrice() {
        QueryWrapper<UserRecharge> wrapper = Wrappers.query();
        wrapper.select("IFNULL(sum(price), 0) as price");
        wrapper.eq("paid", 1);
        return dao.selectOne(wrapper).getPrice();
    }

    /**
     * 根据时间获取充值用户数量
     * @param date 日期
     * @return Integer
     */
    @Override
    public Integer getRechargeUserNumByDate(String date) {
        QueryWrapper<UserRecharge> wrapper = Wrappers.query();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        wrapper.groupBy("uid");
        List<UserRecharge> list = dao.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    /**
     * 根据时间获取充值用户数量
     * @param startDate 日期
     * @param endDate   日期
     * @return Integer
     */
    @Override
    public Integer getRechargeUserNumByPeriod(String startDate, String endDate) {
        QueryWrapper<UserRecharge> wrapper = Wrappers.query();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') between {0} and {1}", startDate, endDate);
        wrapper.groupBy("uid");
        List<UserRecharge> list = dao.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    @Override
    public void operationNowMoney(Integer uid, BigDecimal moneyValue) {
        UserRecharge userRecharge = new UserRecharge();
        String orderId = CrmebUtil.getOrderNo("system");
        userRecharge.setOrderId(orderId);
        userRecharge.setUid(uid);
        userRecharge.setPayTime(DateUtil.nowDateTime());
        userRecharge.setPaid(true);
        userRecharge.setCreateTime(DateUtil.nowDateTime());
        userRecharge.setPrice(moneyValue);
        userRecharge.setGivePrice(BigDecimal.ZERO);
        userRecharge.setRefundPrice(BigDecimal.ZERO);
        userRecharge.setRechargeType(PayConstants.PAY_CHANNEL_SYSTEM_PAY);
        this.save(userRecharge);
    }

    @Override
    public void create(UserRechargeSaveRequest userRechargeSaveRequest) {
        UserRecharge userRecharge;
        if (userRechargeSaveRequest.getUserRechargeId() == null) {
            userRecharge = new UserRecharge();
            String orderId = CrmebUtil.getOrderNo("system");
            userRecharge.setOrderId(orderId);
            userRecharge.setUid(userRechargeSaveRequest.getUid());
            userRecharge.setPayTime(DateUtil.nowDateTime());
            userRecharge.setPaid(false);
            userRecharge.setCreateTime(DateUtil.nowDateTime());
            userRecharge.setGivePrice(BigDecimal.ZERO);
            userRecharge.setRefundPrice(BigDecimal.ZERO);
            userRecharge.setRechargeType(PayConstants.PAY_CHANNEL_SYSTEM_PAY);
        } else {
            userRecharge = getById(userRechargeSaveRequest.getUserRechargeId());
            if (!userRecharge.getStatus().equals(PayConstants.PAY_STATUS_NO)) {
                throw new CrmebException("当前充值单无法操作");
            }
        }
        userRecharge.setPrice(userRechargeSaveRequest.getPrice());
        userRecharge.setUid(userRechargeSaveRequest.getUid());
        userRecharge.setKfRemark(userRechargeSaveRequest.getKfRemark());
        userRecharge.setPaymentVoucherImages(userRechargeSaveRequest.getPaymentVoucherImages());
        userRecharge.setStatus(PayConstants.PAY_STATUS_KF);
        if (userRechargeSaveRequest.getUserRechargeId() == null) {
            this.save(userRecharge);
        } else {
            this.updateById(userRecharge);
        }
    }

    @Override
    public void review(UserRechargeReviewRequest userRechargeReviewRequest) {
        UserRecharge userRecharge = this.getById(userRechargeReviewRequest.getUserRechargeId());
        if (userRecharge == null) {
            throw new CrmebException("充值单不存在");
        }
        if (!userRecharge.getStatus().equals(PayConstants.PAY_STATUS_KF)) {
            throw new CrmebException("当前充值单无法操作");
        }
        if (userRechargeReviewRequest.getAuditStatus().equals(1)) {
            userRecharge.setStatus(PayConstants.PAY_STATUS_CW);
        } else {
            userRecharge.setStatus(PayConstants.PAY_STATUS_NO);
        }
        userRecharge.setFinanceVoucherImages(userRechargeReviewRequest.getFinanceVoucherImages());
        userRecharge.setCwRemark(userRechargeReviewRequest.getCwRemark());
        this.updateById(userRecharge);
    }

    @Override
    public void confirm(UserRechargeConfirmRequest userRechargeConfirmRequest) {
        UserRecharge userRecharge = this.getById(userRechargeConfirmRequest.getUserRechargeId());
        if (userRecharge == null) {
            throw new CrmebException("充值单不存在");
        }
        if (!userRecharge.getStatus().equals(PayConstants.PAY_STATUS_CW)) {
            throw new CrmebException("当前充值单无法操作");
        }
        Boolean execute = transactionTemplate.execute(e -> {
            if (userRechargeConfirmRequest.getAuditStatus().equals(1)) {
                userRecharge.setStatus(PayConstants.PAY_STATUS_CONFIRM);
                userRecharge.setPaid(true);

                User user = userService.getById(userRecharge.getUid());
                // 生成UserBill
                UserBill userBill = new UserBill();
                userBill.setUid(userRecharge.getUid());
                userBill.setLinkId("0");
                userBill.setTitle("后台充值");
                userBill.setCategory(Constants.USER_BILL_CATEGORY_MONEY);
                userBill.setNumber(userRecharge.getPrice());
                userBill.setStatus(1);
                userBill.setCreateTime(DateUtil.nowDateTime());

                userBill.setPm(1);
                userBill.setType(Constants.USER_BILL_TYPE_SYSTEM_ADD);
                userBill.setBalance(user.getNowMoney().add(userRecharge.getPrice()));
                userBill.setMark(StrUtil.format("后台充值增加了{}余额", userRecharge.getPrice()));

                userBillService.save(userBill);
                userService.operationNowMoney(user.getUid(), userRecharge.getPrice(), user.getNowMoney(), "add");

            } else {
                userRecharge.setStatus(PayConstants.PAY_STATUS_NO);
            }
            userRecharge.setRemark(userRechargeConfirmRequest.getRemark());
            this.updateById(userRecharge);
            return Boolean.TRUE;
        });
        if (!execute) {
            throw new CrmebException("充值审批处理异常");
        }
    }
}

