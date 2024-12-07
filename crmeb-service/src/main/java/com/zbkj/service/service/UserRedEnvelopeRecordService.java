package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.user.UserRedEnvelopeRecord;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRedEnvelopeReceiveRequest;
import com.zbkj.common.request.UserRedEnvelopeRecordRequest;
import com.zbkj.common.response.UserRedEnvRecordCountResponse;
import com.zbkj.common.response.UserRedEnvRecordListResponse;
import com.zbkj.common.response.UserRedEnvRecordPriceResponse;
import com.zbkj.common.vo.UserRedEnvelopeRecordVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * UserRedEnvelopeRecordService
 * @author wtj
 * @date 2024/10/31
 */
public interface UserRedEnvelopeRecordService extends IService<UserRedEnvelopeRecord> {

    /**
     * 查询红包收益情况
     * @param request
     * @param pageParamRequest
     * @return
     */
    List<UserRedEnvelopeRecordVo> findPageLList(UserRedEnvelopeRecordRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取个人红包情况
     * @return
     */
    UserRedEnvRecordCountResponse getCount();

    /**
     * 获取我的待领取红包列表
     * @return
     */
    List<UserRedEnvRecordListResponse> getWaitList();

    /**
     * 获取历史红包详情
     * @param pageParamRequest
     * @return
     */
    CommonPage<UserRedEnvelopeRecordVo> getHisList(PageParamRequest pageParamRequest);

    /**
     * 领取红包
     * @param request
     * @return
     */
    UserRedEnvRecordPriceResponse receive(UserRedEnvelopeReceiveRequest request);

    /**
     * 生成红包金额
     * @param userRedenvelopeLevel 用户红包等级
     * @param giftProperty         礼包属性
     * @return 金额
     */
    public BigDecimal generateRedEnvelopePrice(Integer userRedenvelopeLevel, Integer giftProperty);

    /**
     * 自动创建红包
     */
    void autoCreateRedEnv();
}
