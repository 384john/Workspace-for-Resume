package com.nyulink.info;

import java.io.Serializable;

public class VoiceInfo implements Serializable {
	private String qid;
	private String uid;
	private String tid;
	private String uname;
	private String uhead;
	
	private String qvalue;
	private String qlike;
	private String qunlike;
	private String qimg;
	private String qshare;
	// new fields
	private String title;
	private String startdate;
	private String starttime;
	private String enddate;
	private String endtime;
	private String spot;
	private String maxnum;
	private String detail;
	private String commentcount;
	private String joincount;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getSpot() {
		return spot;
	}

	public void setSpot(String spot) {
		this.spot = spot;
	}

	public String getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(String maxnum) {
		this.maxnum = maxnum;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getQshare() {
		return qshare;
	}

	public void setQshare(String qshare) {
		this.qshare = qshare;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUhead() {
		return uhead;
	}

	public void setUhead(String uhead) {
		this.uhead = uhead;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getQvalue() {
		return qvalue;
	}

	public void setQvalue(String qvalue) {
		this.qvalue = qvalue;
	}

	public String getQlike() {
		return qlike;
	}

	public void setQlike(String qlike) {
		this.qlike = qlike;
	}

	public String getQunlike() {
		return qunlike;
	}

	public void setQunlike(String qunlike) {
		this.qunlike = qunlike;
	}

	public String getQimg() {
		return qimg;
	}

	public void setQimg(String qimg) {
		this.qimg = qimg;
	}

	public String getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	public String getJoincount() {
		return joincount;
	}

	public void setJoincount(String joincount) {
		this.joincount = joincount;
	}
}
