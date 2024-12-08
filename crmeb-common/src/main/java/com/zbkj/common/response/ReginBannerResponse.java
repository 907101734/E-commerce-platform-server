package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;

/**
 * ReginBannerResponse对象
 * @author wtj
 * @date 2024/12/08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ReginBannerResponse对象", description = "轮播图返回数据")
public class ReginBannerResponse {

    @ApiModelProperty(value = "门店区banner滚动图")
    private List<HashMap<String, Object>> mdqBanner;

    @ApiModelProperty(value = "生活区banner滚动图")
    private List<HashMap<String, Object>> shqBanner;
}
