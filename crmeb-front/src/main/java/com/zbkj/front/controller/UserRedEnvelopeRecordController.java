package com.zbkj.front.controller;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRedEnvelopeRecordRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.UserRedEnvRecordCountResponse;
import com.zbkj.common.response.UserRedEnvRecordListResponse;
import com.zbkj.common.response.UserRedEnvRecordPriceResponse;
import com.zbkj.common.vo.UserRedEnvelopeRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserRedEnvelopeRecordController
 * @author wtj
 * @date 2024/11/04
 */
@Slf4j
@RestController
@RequestMapping("api/front/user/redEnvelope")
@Api(tags = "营销 -- 红包")
public class UserRedEnvelopeRecordController {

    @ApiOperation(value = "获取待领取红包列表")
    @RequestMapping(value = "/waitList", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserRedEnvRecordListResponse>> getList(@Validated UserRedEnvelopeRecordRequest request,
        @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success();
    }

    @ApiOperation(value = "获取已领取和失效红包列表")
    @RequestMapping(value = "/hisList", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserRedEnvelopeRecordVo>> getHisList(@Validated UserRedEnvelopeRecordRequest request,
        @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success();
    }

    @ApiOperation(value = "获取待领取红包数量、昨日收益、历史收益、当前余额")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public CommonResult<UserRedEnvRecordCountResponse> getCount() {
        return CommonResult.success();
    }

    @ApiOperation(value = "获取红包详情，看完视频才知道红包的金额详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonResult<UserRedEnvRecordPriceResponse> getDetail() {
        return CommonResult.success();
    }

}