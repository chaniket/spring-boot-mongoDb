package com.cb.request;

import org.springframework.data.domain.Sort;

//@Data
public class PageRequest extends org.springframework.data.domain.PageRequest {
	
	public PageRequest(int page, int size, Sort sort) {
		super(page, size, sort);
	
	}

	private static final long serialVersionUID = -2382912865658329097L;
	
	

}
