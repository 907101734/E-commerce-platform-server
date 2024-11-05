package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * UserRedEnvelopeRecordListResponse
 * @author wtj
 * @date 2024/11/04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedEnvRecordListResponse对象", description = "用户红包待领取列表")
public class UserRedEnvRecordListResponse {

    @ApiModelProperty(value = "记录id")
    private Integer id;

    @ApiModelProperty(value = "状态：1-待领取，2-已领取，3-已转入，4-失效")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "关联视频id")
    private String link_ad_id;

    @ApiModelProperty(value = "关联视频地址")
    private String link_ad_addr;

}
