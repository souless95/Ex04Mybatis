package com.edu.mybatis;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MybatisController {
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping("/mybatis/list.do")
	public String list(Model model, HttpServletRequest req) {
		return "07Mybatis/list";
	}
}
