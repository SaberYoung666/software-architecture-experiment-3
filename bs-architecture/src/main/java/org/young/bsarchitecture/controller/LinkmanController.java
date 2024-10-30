package org.young.bsarchitecture.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.young.bsarchitecture.entity.Linkman;
import org.young.bsarchitecture.entity.SaveLinkmanDTO;
import org.young.bsarchitecture.service.impl.LinkmanServiceImpl;

import java.util.List;

/**
 * <p>
 * 联系人表 前端控制器
 * </p>
 *
 * @author saber
 * @since 2024-10-24
 */
@Controller
@Slf4j
public class LinkmanController {

	private final LinkmanServiceImpl linkmanService;

	public LinkmanController(LinkmanServiceImpl linkmanService) {
		this.linkmanService = linkmanService;
	}

	@GetMapping("/")
	public String getLinkmen(Model model) {
		List<Linkman> linkmen = linkmanService.list();
		model.addAttribute("linkmen", linkmen); // 将联系人列表添加到模型中
		model.addAttribute("searchDisplay", "inline");
		model.addAttribute("clearDisplay", "none");
		log.info("刷新联系人表格");
		return "index";
	}

	@GetMapping("/search/{name}")
	public String getLinkmenLikeParam(@PathVariable("name") String name, Model model) {
		// 假设有一个 QueryWrapper 对象，设置查询条件为 age > 25
		QueryWrapper<Linkman> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("name", name);
		List<Linkman> linkmen = linkmanService.list(queryWrapper); // 调用 list 方法
		model.addAttribute("linkmen", linkmen); // 将联系人列表添加到模型中
		model.addAttribute("searchTerm", name);
		model.addAttribute("searchDisplay", "none");
		model.addAttribute("clearDisplay", "inline");
		log.info("展示查询的联系人表格");
		return "index";
	}

	@PutMapping("/update/{contactId}")
	public String updateLinkman(@PathVariable("contactId") Integer contactId, @RequestBody Linkman linkman, Model model) {
		String name = linkman.getName();
		String phoneNumber = linkman.getPhoneNumber();
		UpdateWrapper<Linkman> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("linkman_id", contactId).set("name", name).set("phone_number", phoneNumber);
		boolean result = linkmanService.update(updateWrapper); // 调用 update 方法
		if (result) {
			log.info("更新了联系人：编号：{}，姓名：{}，电话号码：{}",contactId, name, phoneNumber);
		} else {
			log.error("更新联系人错误");
		}
		return getLinkmen(model);
	}

	@DeleteMapping("/delete/{contactId}")
	public String deleteLinkman(@PathVariable("contactId") Integer contactId, Model model) {
		QueryWrapper<Linkman> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("linkman_id", contactId);
		boolean result = linkmanService.remove(queryWrapper); // 调用 remove 方法
		if (result) {
			log.info("删除了联系人：编号：{}",contactId);
		} else {
			log.error("删除联系人错误");
		}
		return getLinkmen(model);
	}

	@PostMapping("/save")
	public String saveLinkman(@RequestBody SaveLinkmanDTO linkmanDTO, Model model) {
		Linkman linkman = new Linkman();
		linkman.setName(linkmanDTO.getName());
		linkman.setPhoneNumber(linkmanDTO.getPhoneNumber());
		boolean result = linkmanService.save(linkman); // 调用 save 方法
		if (result) {
			log.info("更新了联系人：，姓名：{}，电话号码：{}", linkman.getName(), linkman.getPhoneNumber());
		} else {
			log.error("新增联系人错误");
		}
		return getLinkmen(model);
	}
}