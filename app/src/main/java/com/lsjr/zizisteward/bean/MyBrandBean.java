package com.lsjr.zizisteward.bean;

import java.util.List;

public class MyBrandBean {

	private String error;
	private String msg;
	private pageBrand pageBrand;

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

	public pageBrand getPageBrand() {
		return pageBrand;
	}

	public void setPageBrand(pageBrand pageBrand) {
		this.pageBrand = pageBrand;
	}

	public class pageBrand {

		private List<page> page;

		public List<page> getPage() {
			return page;
		}

		public void setPage(List<page> page) {
			this.page = page;
		}

		public class page {
			private String id;
			private String blogo;
			private String bname;
			private boolean space;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getBlogo() {
				return blogo;
			}

			public void setBlogo(String blogo) {
				this.blogo = blogo;
			}

			public String getBname() {
				return bname;
			}

			public void setBname(String bname) {
				this.bname = bname;
			}

			public boolean isSpace() {
				return space;
			}

			public void setSpace(boolean space) {
				this.space = space;
			}

		}
	}
}
