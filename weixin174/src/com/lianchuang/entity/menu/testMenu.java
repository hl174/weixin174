package com.lianchuang.entity.menu;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlRootElement
public class testMenu {
	private String menuName;
	private String menuUrl;
	private String userName;
	private String timestamp;
	
	@XmlElement
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	@XmlElement
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	@XmlElement
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@XmlElement
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	public static void main(String[] args) {
		 JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(testMenu.class);
			 Marshaller ms = jc.createMarshaller();  
			 testMenu t = new testMenu();
				t.setMenuName("baidu");
				t.setMenuUrl("http://www.baidu.com");
				t.setTimestamp("121212132");
				t.setUserName("zhangsan");
				//System.out.println(t);
				 ms.marshal(t, System.out);  
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	      
		
	}
}
