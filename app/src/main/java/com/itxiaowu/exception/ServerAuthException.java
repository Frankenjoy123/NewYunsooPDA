package com.itxiaowu.exception;

@SuppressWarnings("serial")
public class ServerAuthException extends BaseException {
	
	public ServerAuthException()
	{
		super("It's login required.");
	}

	boolean loginRequired;
	public ServerAuthException(String msg, boolean loginRequired)
	{
		super(msg);
		this.loginRequired = loginRequired;
	}

	public boolean isLoginRequired() {
		return loginRequired;
	}
}
