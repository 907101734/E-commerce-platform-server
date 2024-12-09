package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * UserRedVideoResponse
 * 获取待领取的红包视频信息
 * @author wtj
 * @date 2024/11/04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedVideoResponse对象", description = "视频信息")
public class UserRedVideoResponse {

    @ApiModelProperty(value = "关联视频id")
    private Integer linkAdId;

    @ApiModelProperty(value = "关联视频地址")
    private String linkAdAddr;
}
