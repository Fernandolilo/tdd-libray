package com.systempro.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {

	private Long id;
	@NotEmpty
	private String isbn;
	@NotEmpty
	private String customer;
	@NotEmpty
	@Email
	private String email;
	private BookDTO book;
}
