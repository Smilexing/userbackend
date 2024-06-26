package com.smile.userbackend.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)        //由mybatisX生成会自动生成该注解，别忘了加
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
            
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
            
     */
    private String userPassword;

    /**
     * 电话号码
            
     */
    private String phone;

    /**
     * 邮箱
            
     */
    private String email;

    /**
     * 状态 0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;


    /**
     * 逻辑删除
     */
    @TableLogic     //一定要加上这个注解，才代表逻辑删除
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}