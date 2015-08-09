package com.lianchuang.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lianchuang.entity.autoReply.MessageResponse;
import com.lianchuang.entity.message.resp.TextMessage;
import com.lianchuang.util.MessageUtil;
import com.lianchuang.util.WeixinUtil;
import com.weixin.robot.FormatXmlProcess;
import com.weixin.robot.TulingApiProcess;

public class CoreService {

	
	static Logger logger = LoggerFactory.getLogger(CoreService.class);
	
	//显示二维码
	public static void getCode( HttpServletResponse response,String ticket) throws IOException{
		String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
		requestUrl = requestUrl.replace("TICKET", ticket);
		response.sendRedirect(requestUrl);
	}
	//获取ticket
	public static String getQr(String accessToken, String jsonMsg){
			String result = null;
			String requestUrl = " https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
			requestUrl = requestUrl.replace("TOKEN", accessToken);
			JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", jsonMsg);
			if(null != jsonObject){
				System.out.println(jsonObject);
				result = jsonObject.getString("ticket");
			}
			return result;
		}
	//用ticket获取二维码
		public static boolean chageQr(String ticket){
			boolean result = false;
			String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
			requestUrl = requestUrl.replace("TICKET", ticket);
			JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);
			System.out.println(jsonObject+"");
			if(null != jsonObject){
				System.out.println(jsonObject);
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				if(0 == errorCode){
					result = true;
					logger.info("成功errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
					System.out.println("成功errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
				}else{
					logger.info("失败errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
					System.out.println("失败errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
				}
			}
			return result;
		}
		
		
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

			// xml请求解析
			// 调用消息工具类MessageUtil解析微信发来的xml格式的消息，解析的结果放在HashMap里；
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 从HashMap中取出消息中的字段；
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息内容
			String content = requestMap.get("Content");
			// 从HashMap中取出消息中的字段；
			
			// 回复文本消息
						TextMessage textMessage = new TextMessage();
						textMessage.setToUserName(fromUserName);
						textMessage.setFromUserName(toUserName);
						textMessage.setCreateTime(new Date().getTime());
						textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
						textMessage.setFuncFlag(0);

						// 文本消息
						if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
							respContent = "您发送的是文本消息！罗鹏会为您贴心服务、哈哈";
						}
						// 图片消息
						else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
							respContent = "您发送的是图片消息！罗鹏会为您贴心服务、哈哈";
						}
						// 地理位置消息
						else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
							respContent = "您发送的是地理位置消息！罗鹏会为您贴心服务、哈哈";
						}
						// 链接消息
						else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
							respContent = "您发送的是链接消息！罗鹏会为您贴心服务、哈哈";
						}
						// 音频消息
						else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
							respContent = "您发送的是音频消息！罗鹏会为您贴心服务、哈哈";
						}
						// 事件推送
						else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
							// 事件类型
							String eventType = requestMap.get("Event");
							// 订阅
							if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
								respContent = "谢谢您的关注！罗鹏会为您贴心服务、哈哈";
							}
							// 取消订阅
							else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
								// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
							}
							// 自定义菜单点击事件
							else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
								// TODO 自定义菜单权没有开放，暂不处理该类消息
							}
						}

						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
			/*// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				//微信聊天机器人测试 2015-3-31
				if(content!=null){
					respContent = TulingApiProcess.getTulingResult(content);
					if(respContent==""||null==respContent){
						MessageResponse.getTextMessage(fromUserName , toUserName , "服务号暂时无法回复，请稍后再试！");
					}
					//return FormatXmlProcess.formatXmlAnswer(toUserName, fromUserName, respContent);
					return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
				}
//				else if (content.startsWith("ZY")) {//查作业
//					String keyWord = content.replaceAll("^ZY" , "").trim();
//					if ("".equals(keyWord)) {
//						respContent = AutoReply.getXxtUsage("24");
//					} else {
//						return XxtService.getHomework("24" , fromUserName , toUserName , keyWord);
//					}
//				} else if (content.startsWith("SJX")) {//收件箱
//					String keyWord = content.replaceAll("^SJX" , "").trim();
//					if ("".equals(keyWord)) {
//						respContent = AutoReply.getXxtUsage("25");
//					} else {
//						return XxtService.getRecvBox("25" , fromUserName , toUserName , keyWord);
//					}
//				}
//				return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送
				String eventType = requestMap.get("Event");// 事件类型
				
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {// 订阅
					respContent = "欢迎关注沪动校讯通！";
					return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单点击事件
					String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					logger.info("eventKey is:" +eventKey);
					return MenuClickService.getClickResponse(eventKey , fromUserName , toUserName);
				}
			}
			
			
			
			
			
			//开启微信声音识别测试 2015-3-30
			else if(msgType.equals("voice"))
			{
				String recvMessage = requestMap.get("Recognition");
				//respContent = "收到的语音解析结果："+recvMessage;
				if(recvMessage!=null){
					respContent = TulingApiProcess.getTulingResult(recvMessage);
				}else{
					respContent = "您说的太模糊了，能不能重新说下呢？";
				}
				return MessageResponse.getTextMessage(fromUserName , toUserName , respContent); 
			}
			//拍照功能
			else if(msgType.equals("pic_sysphoto"))
			{
				
			}
			else
			{
				return MessageResponse.getTextMessage(fromUserName , toUserName , "返回为空"); 
			}
			
			*/
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}

}
