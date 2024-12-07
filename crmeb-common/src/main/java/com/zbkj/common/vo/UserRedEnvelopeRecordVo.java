package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * UserRedEnvelopeRecordVo
 * @author wtj
 * @date 2024/10/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedEnvelopeRecordVo对象", description = "用户红包账单表")
public class UserRedEnvelopeRecordVo {

    @ApiModelProperty(value = "记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户uid")
    private Integer uid;

    @ApiModelProperty(value = "关联id（orderNo）")
    private Integer redEnvelopeId;

    @ApiModelProperty(value = "类型：1-增加，2-扣减（提现）")
    private Integer type;

    @ApiModelProperty(value = "金额")
    private BigDecimal price;

    @ApiModelProperty(value = "备注")
    private String mark;

    @ApiModelProperty(value = "状态：0-待领取，1-已领取，2-已转入，3-失效")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "领取时间")
    private Date receiveTime;

    @ApiModelProperty(value = "关联视频id")
    private String linkAdId;

    @ApiModelProperty(value = "关联视频地址")
    private String linkAdAddr;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

}
