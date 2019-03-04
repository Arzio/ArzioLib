package com.arzio.arziolib.api.event;

public interface PostEvent {

	/**
	 * This method must be called by the event caller <b>only if the event is not cancelled</b>.
	 */
	public void afterPost();
}
