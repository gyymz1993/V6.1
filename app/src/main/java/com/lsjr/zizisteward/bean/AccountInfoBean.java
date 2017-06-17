package com.lsjr.zizisteward.bean;

import java.io.Serializable;

public class AccountInfoBean implements Serializable {
	private int bid_amount;
	private int bid_count;
	private int entityId;
	private float freeze;
	private int id;
	private int invest_amount;
	private int invest_count;
	private boolean persistent;
	private float receive_amount;
	private int repayment_amount;
	private String sign;
	private float user_account;
	private float user_account2;

	public int getBid_amount() {
		return bid_amount;
	}

	public void setBid_amount(int bid_amount) {
		this.bid_amount = bid_amount;
	}

	public int getBid_count() {
		return bid_count;
	}

	public void setBid_count(int bid_count) {
		this.bid_count = bid_count;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public float getFreeze() {
		return freeze;
	}

	public void setFreeze(float freeze) {
		this.freeze = freeze;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInvest_amount() {
		return invest_amount;
	}

	public void setInvest_amount(int invest_amount) {
		this.invest_amount = invest_amount;
	}

	public int getInvest_count() {
		return invest_count;
	}

	public void setInvest_count(int invest_count) {
		this.invest_count = invest_count;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public float getReceive_amount() {
		return receive_amount;
	}

	public void setReceive_amount(float receive_amount) {
		this.receive_amount = receive_amount;
	}

	public int getRepayment_amount() {
		return repayment_amount;
	}

	public void setRepayment_amount(int repayment_amount) {
		this.repayment_amount = repayment_amount;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public float getUser_account() {
		return user_account;
	}

	public void setUser_account(float user_account) {
		this.user_account = user_account;
	}

	public float getUser_account2() {
		return user_account2;
	}

	public void setUser_account2(float user_account2) {
		this.user_account2 = user_account2;
	}

}
