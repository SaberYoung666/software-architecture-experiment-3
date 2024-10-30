package org.young.bsarchitecture.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SaveLinkmanDTO implements Serializable {
	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 手机号码
	 */
	private String phoneNumber;
}
