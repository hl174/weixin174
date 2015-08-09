package com.lianchuang.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class LoginController {
	@RequestMapping("/hello")
	public String hello(HttpSession session){
		session.setAttribute("LOGIN_USER", "zhang");
		System.out.println("/hello");
		return "hello";
	}
	@RequestMapping(value="/aaa")
	@ResponseBody
	public String aaa(){
		System.out.println("/aaa");
		return "测试乱码";
	}
}
