package com.zbkj.admin.controller;

import com.zbkj.admin.service.AdminLoginService;
import com.zbkj.common.constants.PayConstants;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserRechargeConfirmRequest;
import com.zbkj.common.request.UserRechargeReviewRequest;
import com.zbkj.common.request.UserRechargeSaveRequest;
import com.zbkj.common.request.UserRechargeSearchRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.SystemAdminResponse;
import com.zbkj.common.response.UserRechargeResponse;
import com.zbkj.service.service.UserRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 用户充值表 前端控制器
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
@Slf4j
@RestController
@RequestMapping("api/admin/user/topUpLog")
@Api(tags = "财务 -- 充值")
public class UserRechargeController {

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 分页显示用户充值表
     * @param request          搜索条件
     * @param pageParamRequest 分页参数
     */
    @PreAuthorize("hasAuthority('admin:recharge:list')")
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserRechargeResponse>> getList(@Validated UserRechargeSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        SystemAdminResponse infoByToken = adminLoginService.getInfoByToken();
        String right = "";
        if (infoByToken.getPermissionsList().contains("admin:recharge:create")) { //客服
            right = PayConstants.PAY_ROLE_KF;
        } else if (infoByToken.getPermissionsList().contains("admin:recharge:review")) { //财务
            right = PayConstants.PAY_ROLE_CW;
        } else if (infoByToken.getPermissionsList().contains("admin:recharge:confirm")) { //管理员
            right = PayConstants.PAY_ROLE_GLY;
        }
        CommonPage<UserRechargeResponse> userRechargeCommonPage = CommonPage.restPage(userRechargeService.getList(request, pageParamRequest, right));
        return CommonResult.success(userRechargeCommonPage);
    }

    @PreAuthorize("hasAuthority('admin:recharge:create')")
    @ApiOperation(value = "提交充值单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Void> create(@Validated @RequestBody UserRechargeSaveRequest userRechargeSaveRequest) {
        userRechargeService.create(userRechargeSaveRequest);
        return CommonResult.success();
    }

    @PreAuthorize("hasAuthority('admin:recharge:review')")
    @ApiOperation(value = "财务审查充值单")
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public CommonResult<Void> review(@Validated @RequestBody UserRechargeReviewRequest userRechargeReviewRequest) {
        userRechargeService.review(userRechargeReviewRequest);
        return CommonResult.success();
    }

    @PreAuthorize("hasAuthority('admin:recharge:confirm')")
    @ApiOperation(value = "确认充值单")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public CommonResult<Void> confirm(@Validated @RequestBody UserRechargeConfirmRequest userRechargeConfirmRequest) {
        userRechargeService.confirm(userRechargeConfirmRequest);
        return CommonResult.success();
    }

    /**
     * 充值总金额
     */
    @PreAuthorize("hasAuthority('admin:recharge:balance')")
    @ApiOperation(value = "充值总金额")
    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    public CommonResult<HashMap<String, BigDecimal>> balance() {
        return CommonResult.success(userRechargeService.getBalanceList());
    }
}



