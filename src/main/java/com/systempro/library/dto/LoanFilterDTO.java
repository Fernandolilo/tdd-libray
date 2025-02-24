package com.systempro.library.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoanFilterDTO {

	private String isbn;
	private String customer;
	
}
