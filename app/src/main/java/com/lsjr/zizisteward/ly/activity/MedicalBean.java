package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class MedicalBean {

	private String msg;
	private String error;
	private List<Medicalcombination> Medicalcombination;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Medicalcombination> getMedicalcombination() {
		return Medicalcombination;
	}

	public void setMedicalcombination(
			List<Medicalcombination> medicalcombination) {
		Medicalcombination = medicalcombination;
	}

	public class Medicalcombination {
		private String bname;
		private String id;
		private String sname;
		private String cost_price;
		private String spicfirst;
		private String sprice;
		private String theme_name;

		public String getBname() {
			return bname;
		}

		public void setBname(String bname) {
			this.bname = bname;
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

		public String getCost_price() {
			return cost_price;
		}

		public void setCost_price(String cost_price) {
			this.cost_price = cost_price;
		}

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getSprice() {
			return sprice;
		}

		public void setSprice(String sprice) {
			this.sprice = sprice;
		}

		public String getTheme_name() {
			return theme_name;
		}

		public void setTheme_name(String theme_name) {
			this.theme_name = theme_name;
		}
	}
}
