package com.zbkj.common.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * UserRedEnvelope
 * @author wtj
 * @date 2024/10/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_user_red_envelope")
@ApiModel(value = "UserRedEnvelope对象", description = "用户红包表")
public class UserRedEnvelope {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户uid")
    private Integer uid;

    @ApiModelProperty(value = "关联id（orderNo,提现id）")
    private String linkId;

    @ApiModelProperty(value = "关联类型（order,extract，yue）")
    private String linkType;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "物品价格")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "红包总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "红包剩余总金额")
    private BigDecimal surplusAmount;

    @ApiModelProperty(value = "状态：1-领取中，2-已领完")
    private Integer status;

    @ApiModelProperty(value = "礼包属性 1-699，2-999")
    private Integer giftProperty;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
