package com.lianchuang.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianchuang.entity.WeChat;
import com.lianchuang.service.CoreService;
import com.lianchuang.util.SignUtil;
import com.lianchuang.util.WeixinUtil;

@Controller
@RequestMapping("/xxt")
public class XxtController {
	
	@RequestMapping(value="/api",method = RequestMethod.GET)
	@ResponseBody
	public void xxtInterface(HttpServletRequest request, HttpServletResponse response) throws IOException{
		// 微信加密签名  
        String signature = request.getParameter("signature");  
        // 时间戳  
        String timestamp = request.getParameter("timestamp");  
        // 随机数  
        String nonce = request.getParameter("nonce");  
        // 随机字符串  
        String echostr = request.getParameter("echostr");
  
        PrintWriter out = response.getWriter();  
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败  
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {  
            out.print(echostr);  
        }  
        out.close();  
        out = null;   
	}
	
	@RequestMapping(value="/api",method = RequestMethod.POST)
	@ResponseBody
	public String getWeiXinMessage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
        //初始化配置文件
        String respMessage = CoreService.processRequest(request);//调用CoreService类的processRequest方法接收、处理消息，并得到处理结果；
        
        // 响应消息  
        //调用response.getWriter().write()方法将消息的处理结果返回给用户
        return respMessage;
	}
	//生成医生二维码
	@RequestMapping(value="/test",method = RequestMethod.GET)
	@ResponseBody
	public String getTest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		 String ticket="";
		 String code="";
		try {
			// 将请求、响应的编 码均设置为UTF-8（防止中文乱码）  

			request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
	        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
	        //获取access_token
	        String access_token=WeixinUtil.getAccessToken("wxfd01b6e24ad82e51", "8ff432e91a902811d51a70e158dc6dc5").getAccessToken();
	        //获取ticket
	        ticket=CoreService.getQr(access_token, "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\":01001}}}");
	        //获取
	      CoreService.getCode(response,ticket);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return  code;
	}
	
	
	
	public static void main(String[] args) {
		
		String signature = "E56004E954970049C37E8E3413432A7BB17D1A93"; // 微信加密签名  
        String timestamp = "12131"; // 时间戳  
        String nonce = "32432";// 随机数  
        String echostr = "fddsads";// 随机字符串  
  
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败  
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {  
        	System.out.println("接入成功");
        } else {  
            System.out.println("不是微信服务器发来的请求,请小心!");  
        }  
	}
}
