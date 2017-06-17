package com.lsjr.zizisteward.ly.activity;

import java.util.List;

import com.lsjr.zizisteward.activity.SettingActivity;

public class ListChatBean {

	private List<ListChat> listChats;

	public List<ListChat> getListChats() {
		return listChats;
	}

	public void setListChats(List<ListChat> listChats) {
		this.listChats = listChats;
	}

	public static class ListChat {
		private String chatType;
		private String userName;
		private String m_content;
		private String m_type;
		private String m_time;
		private int Unread;
		private int AllMsg;
		private String nike;
		private String photo;
		
		public String getNike() {
			return nike;
		}
		
		public void setNike(String nike) {
			this.nike = nike;
		}
		
		public String getPhoto() {
			return photo;
		}
		
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		
		public int getAllMsg() {
			return AllMsg;
		}
		
		public void setAllMsg(int allMsg) {
			AllMsg = allMsg;
		}
		
		public int getUnread() {
			return Unread;
		}
		
		public void setUnread(int unread) {
			Unread = unread;
		}

		public String getChatType() {
			return chatType;
		}

		public void setChatType(String chatType) {
			this.chatType = chatType;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getM_content() {
			return m_content;
		}

		public void setM_content(String m_content) {
			this.m_content = m_content;
		}

		public String getM_type() {
			return m_type;
		}

		public void setM_type(String m_type) {
			this.m_type = m_type;
		}

		public String getM_time() {
			return m_time;
		}

		public void setM_time(String m_time) {
			this.m_time = m_time;
		}
	}
}
