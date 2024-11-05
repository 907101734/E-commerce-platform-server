package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * UserRedEnvRecordHisListResponse
 * @author wtj
 * @date 2024/11/04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedEnvRecordHisListResponse对象", description = "用户红包已领取领取列表")
public class UserRedEnvRecordHisListResponse {

    @ApiModelProperty(value = "记录id")
    private Integer id;

    @ApiModelProperty(value = "用户uid")
    private Integer uid;

    @ApiModelProperty(value = "关联id（orderNo）")
    private String linkId;

    @ApiModelProperty(value = "关联类型（order）")
    private String linkType;

    @ApiModelProperty(value = "类型：1-增加，2-扣减（提现）")
    private Integer type;

    @ApiModelProperty(value = "金额")
    private BigDecimal price;

    @ApiModelProperty(value = "状态：1-待领取，2-已领取，3-已转入，4-失效")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "关联视频id")
    private String link_ad_id;

    @ApiModelProperty(value = "关联视频地址")
    private String link_ad_addr;

}
