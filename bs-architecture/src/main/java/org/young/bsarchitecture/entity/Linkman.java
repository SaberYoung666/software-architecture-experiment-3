package org.young.bsarchitecture.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 联系人表
 * </p>
 *
 * @author saber
 * @since 2024-10-24
 */
@Getter
@Setter
public class Linkman implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 联系人编号
     */
    @TableId(value = "linkman_id", type = IdType.AUTO)
    private Integer linkmanId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phoneNumber;
}
