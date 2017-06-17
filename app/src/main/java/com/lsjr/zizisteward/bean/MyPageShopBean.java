package com.lsjr.zizisteward.bean;

import java.util.List;

public class MyPageShopBean {

	private String error;
	private String msg;
	private pageShop pageShop;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public pageShop getPageShop() {
		return pageShop;
	}

	public void setPageShop(pageShop pageShop) {
		this.pageShop = pageShop;
	}

	public class pageShop {

		private List<page> page;

		public List<page> getPage() {
			return page;
		}
		
		public void setPage(List<page> page) {
			this.page = page;
		}

		public class page {
			
			private String id;
			private String sname;
			private String spic;
			private String sprice;
			private boolean space;

			public boolean isSpace() {
				return space;
			}
			
			public void setSpace(boolean space) {
				this.space = space;
			}
			
			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getSname() {
				return sname;
			}

			public void setSname(String sname) {
				this.sname = sname;
			}

			public String getSpic() {
				return spic;
			}

			public void setSpic(String spic) {
				this.spic = spic;
			}

			public String getSprice() {
				return sprice;
			}

			public void setSprice(String sprice) {
				this.sprice = sprice;
			}

		}
	}

}
