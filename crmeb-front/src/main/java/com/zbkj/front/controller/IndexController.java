package com.zbkj.front.controller;

import com.zbkj.common.enums.RegionEnum;
import com.zbkj.common.model.system.SystemConfig;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.IndexInfoResponse;
import com.zbkj.common.response.IndexProductResponse;
import com.zbkj.common.response.InformationResponse;
import com.zbkj.common.response.ReginBannerResponse;
import com.zbkj.front.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 -- 用户中心
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
@RestController("IndexController")
@RequestMapping("api/front")
@Api(tags = "首页")
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 首页数据
     */
    @ApiOperation(value = "首页数据")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public CommonResult<IndexInfoResponse> getIndexInfo() {
        return CommonResult.success(indexService.getIndexInfo());
    }

    /**
     * 轮播图数据
     * @return
     */
    @ApiOperation(value = "轮播图数据")
    @RequestMapping(value = "/reginBanner", method = RequestMethod.GET)
    public CommonResult<ReginBannerResponse> getReginBanner() {
        return CommonResult.success(indexService.getReginBanner());
    }

    /**
     * 首页商品列表
     */
    /**
     * 首页商品列表
     */
    @ApiOperation(value = "首页商品列表")
    @RequestMapping(value = "/index/product/{type}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "type", value = "类型 【1 精品推荐 2 热门榜单 3首发新品 4促销单品】", dataType = "int", required = true)
    public CommonResult<CommonPage<IndexProductResponse>> getProductList(@PathVariable(value = "type") Integer type, PageParamRequest pageParamRequest) {
        return CommonResult.success(indexService.findIndexProductList(type, -1, null, null, pageParamRequest));
    }

    @ApiOperation(value = "首页报单区商品列表")
    @RequestMapping(value = "/index/bdq/product", method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryId", value = "分类id", dataType = "int", required = false),
        @ApiImplicitParam(name = "giftProperty", value = "礼包属性 1-699 2-999 3-1999 4-2999", dataType = "int", required = false)})
    public CommonResult<CommonPage<IndexProductResponse>> getT1ProductList(
        @RequestParam(value = "categoryId", required = false) Integer categoryId,
        @RequestParam(name = "giftProperty", required = false) Integer giftProperty,
        PageParamRequest pageParamRequest) {
        return CommonResult.success(indexService.findIndexProductList(null, RegionEnum.REGION_BDQ.getType(), categoryId, giftProperty, pageParamRequest));
    }

    @ApiOperation(value = "首页生活区商品列表")
    @RequestMapping(value = "/index/shq/product", method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryId", value = "分类id", dataType = "int", required = false)})
    public CommonResult<CommonPage<IndexProductResponse>> getT2ProductList(
        @RequestParam(value = "categoryId", required = false) Integer categoryId,
        PageParamRequest pageParamRequest) {
        return CommonResult.success(indexService.findIndexProductList(null, RegionEnum.REGION_SHQ.getType(), categoryId, null, pageParamRequest));
    }

    @ApiOperation(value = "首页门店区商品列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryId", value = "分类id", dataType = "int", required = false)})
    @RequestMapping(value = "/index/mdq/product", method = RequestMethod.GET)
    public CommonResult<CommonPage<IndexProductResponse>> getT3ProductList(
        @RequestParam(value = "categoryId", required = false) Integer categoryId,
        PageParamRequest pageParamRequest) {
        return CommonResult.success(indexService.findIndexProductList(null, RegionEnum.REGION_MDQ.getType(), categoryId, null, pageParamRequest));
    }

    /**
     * 热门搜索
     */
    @ApiOperation(value = "热门搜索")
    @RequestMapping(value = "/search/keyword", method = RequestMethod.GET)
    public CommonResult<List<HashMap<String, Object>>> hotKeywords() {
        return CommonResult.success(indexService.hotKeywords());
    }

    /**
     * 分享配置
     */
    @ApiOperation(value = "分享配置")
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> share() {
        return CommonResult.success(indexService.getShareConfig());
    }

    /**
     * 颜色配置
     */
    @ApiOperation(value = "颜色配置")
    @RequestMapping(value = "/index/color/config", method = RequestMethod.GET)
    public CommonResult<SystemConfig> getColorConfig() {
        return CommonResult.success(indexService.getColorConfig());
    }

    /**
     * 版本信息
     */
    @ApiOperation(value = "获取版本信息")
    @RequestMapping(value = "/index/get/version", method = RequestMethod.GET)
    public CommonResult<Map<String, Object>> getVersion() {
        return CommonResult.success(indexService.getVersion());
    }

    /**
     * 全局本地图片域名
     */
    @ApiOperation(value = "全局本地图片域名")
    @RequestMapping(value = "/image/domain", method = RequestMethod.GET)
    public CommonResult<String> getImageDomain() {
        return CommonResult.success(indexService.getImageDomain(), "成功");
    }

    @ApiOperation(value = "企业信息")
    @RequestMapping(value = "/index/information", method = RequestMethod.GET)
    public CommonResult<InformationResponse> information() {
        return CommonResult.success(indexService.getInformation());
    }

}



