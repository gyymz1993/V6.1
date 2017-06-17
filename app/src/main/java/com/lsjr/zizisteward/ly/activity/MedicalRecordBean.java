package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class MedicalRecordBean {

	private List<MedicalRecord> mRecords;

	public List<MedicalRecord> getmRecords() {
		return mRecords;
	}

	public void setmRecords(List<MedicalRecord> mRecords) {
		this.mRecords = mRecords;
	}

	public static class MedicalRecord {
		private int type;
		private String title;
		private String company_name;
		private String people_name;
		private String people_sex;
		private int space;
		private int ly;
		private String id;
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public int getLy() {
			return ly;
		}
		
		public void setLy(int ly) {
			this.ly = ly;
		}

		public int getSpace() {
			return space;
		}
		
		public void setSpace(int space) {
			this.space = space;
		}
		
		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getCompany_name() {
			return company_name;
		}

		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}

		public String getPeople_name() {
			return people_name;
		}

		public void setPeople_name(String people_name) {
			this.people_name = people_name;
		}

		public String getPeople_sex() {
			return people_sex;
		}

		public void setPeople_sex(String people_sex) {
			this.people_sex = people_sex;
		}
	}
}
