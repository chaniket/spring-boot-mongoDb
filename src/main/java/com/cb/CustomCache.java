package com.cb;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomCache {
	private long timeToLeave;
	private long timeToLeaveTmp;

	private CustomCache(long timeToLeave) {
		super();
		this.timeToLeave = timeToLeave;
	}

	public static void main(String[] args) {
	}
}
