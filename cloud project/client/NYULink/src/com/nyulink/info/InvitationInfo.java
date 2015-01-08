package com.nyulink.info;

import java.io.Serializable;

public class InvitationInfo implements Serializable {
	private String id;
	private String senduid;
	private String receiveuid;
	private String invitetime;
	private String responsetime;
	private String status;
	
	private String userid;
	private String uname;
	private String uhead;
	private String uage;
	private String uhobbles;
	private String uplace;
	private String uexplain;
	private String utime;
	private String ubrand;
	private String usex;
	private String uemail;
	private String ufacebook;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSenduid() {
		return senduid;
	}
	public void setSenduid(String senduid) {
		this.senduid = senduid;
	}
	public String getReceiveuid() {
		return receiveuid;
	}
	public void setReceiveuid(String receiveuid) {
		this.receiveuid = receiveuid;
	}
	public String getInvitetime() {
		return invitetime;
	}
	public void setInvitetime(String invitetime) {
		this.invitetime = invitetime;
	}
	public String getResponsetime() {
		return responsetime;
	}
	public void setResponsetime(String responsetime) {
		this.responsetime = responsetime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public String getUsex() {
		return usex;
	}

	public void setUsex(String usex) {
		this.usex = usex;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUage() {
		return uage;
	}

	public void setUage(String uage) {
		this.uage = uage;
	}

	public String getUhobbles() {
		return uhobbles;
	}

	public void setUhobbles(String uhobbles) {
		this.uhobbles = uhobbles;
	}

	public String getUplace() {
		return uplace;
	}

	public void setUplace(String uplace) {
		this.uplace = uplace;
	}

	public String getUexplain() {
		return uexplain;
	}

	public void setUexplain(String uexplain) {
		this.uexplain = uexplain;
	}

	public String getUtime() {
		return utime;
	}

	public void setUtime(String utime) {
		this.utime = utime;
	}

	public String getUbrand() {
		return ubrand;
	}

	public void setUbrand(String ubrand) {
		this.ubrand = ubrand;
	}

	public String getUhead() {
		return uhead;
	}

	public void setUhead(String uhead) {
		this.uhead = uhead;
	}
	
	public String getUemail() {
		return uemail;
	}

	public void setUemail(String uemail) {
		this.uemail = uemail;
	}
	public String getUfacebook() {
		return ufacebook;
	}
	public void setUfacebook(String ufacebook) {
		this.ufacebook = ufacebook;
	}
	
}
