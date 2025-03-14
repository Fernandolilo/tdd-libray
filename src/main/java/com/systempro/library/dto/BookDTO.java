package com.systempro.library.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

	
	private Long id;
	@NotEmpty
	private String title;
	@NotEmpty
	private String autor;
	@NotEmpty
	private String isbn;
}
