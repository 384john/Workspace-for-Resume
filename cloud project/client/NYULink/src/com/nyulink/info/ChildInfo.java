package com.nyulink.info;

import java.io.Serializable;

public class ChildInfo implements Serializable {
	private String childid;
	private String userid;
	private String cname;
	private String chead;
	private String cage;
	private String chobbles;
	private String csex;
	private String cdetail;

	public String getChildid() {
		return childid;
	}

	public void setChildid(String childid) {
		this.childid = childid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getChead() {
		return chead;
	}

	public void setChead(String chead) {
		this.chead = chead;
	}

	public String getCage() {
		return cage;
	}

	public void setCage(String cage) {
		this.cage = cage;
	}

	public String getChobbles() {
		return chobbles;
	}

	public void setChobbles(String chobbles) {
		this.chobbles = chobbles;
	}

	public String getCdetail() {
		return cdetail;
	}

	public void setCdetail(String cdetail) {
		this.cdetail = cdetail;
	}

	public String getCsex() {
		return csex;
	}

	public void setCsex(String csex) {
		this.csex = csex;
	}

}
