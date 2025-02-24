package com.systempro.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.library.dto.LoanFilterDTO;
import com.systempro.library.entity.Book;
import com.systempro.library.entity.Loan;
import com.systempro.library.exceptions.BusinessException;
import com.systempro.library.repository.LoanRepository;
import com.systempro.library.service.imp.LoanServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

	LoanService service;

	@MockBean
	LoanRepository repository;

	@BeforeEach
	public void setUp() {
		this.service = new LoanServiceImpl(repository);
	}

	@Test
	@DisplayName("Deve Salvar um emprestimo")
	public void saveLoanTest() {
		Book book = Book.builder().id(1L).build();
		String customer = "Fulano";

		Loan savingLoan = Loan.builder().book(book).customer(customer).instante(LocalDate.now()).build();

		Loan savedLoan = Loan.builder().id(1L).instante(LocalDate.now()).customer(customer).book(book).build();

		when(repository.save(savingLoan)).thenReturn(savedLoan);

		Loan loan = service.save(savingLoan);

		assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(loan.getInstante()).isEqualTo(savedLoan.getInstante());

		assertThat(loan.getId()).isEqualTo(savedLoan.getId());

	}

	@Test
	@DisplayName("Deve lançar erro de negocio ao salvar um emprestimo ja emprestado")
	public void invalidSaveLoanTest() {

		Loan savingLoan = createLoan();

		when(repository.existsByBookAndNotReturned(savingLoan.getBook())).thenReturn(true);

		// When (execução)
		Throwable exception = catchException(() -> service.save(savingLoan));

		// Then (verificação)
		assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Book already loaned");

		// Garante que o método save NUNCA é chamado se a exceção for lançada
		verify(repository, never()).save(savingLoan);
	}

	@Test
	@DisplayName("Deve obter as informações de um emprestimo pelo ID")
	public void getLoanDatailsTest() {
		// cenario

		Long id = 1L;

		Loan loan = createLoan();
		loan.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(loan));

		// exec
		Optional<Loan> result = service.getById(id);

		// verificacai

		assertThat(result.isPresent()).isTrue();

		assertThat(result.get().getId()).isEqualTo(id);
		assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
		assertThat(result.get().getBook()).isEqualTo(loan.getBook());
		assertThat(result.get().getInstante()).isEqualTo(loan.getInstante());

		verify(repository).findById(id);
	}

	@Test
	@DisplayName("Deve atualiar um emprestimo")
	public void updateLoanTest() {

		// cenario

		Loan loan = createLoan();
		loan.setId(1L);
		loan.setReturned(true);
		
		//mock para verificação simples
		when(repository.save(loan)).thenReturn( loan );
		
		Loan updatedloan = service.update(loan);
		
		assertThat(updatedloan.getReturned()).isTrue();
		
		verify( repository).save(loan);
		
	}
	
	@Test
	@DisplayName("Deve filtrar livro pela propriedade")
	public void findLoanTest() {
		//cenario
		
		LoanFilterDTO filterDTO = LoanFilterDTO.builder().customer("FUlano").isbn("321").build();
		
		Loan loan = createLoan();
		Long id = 1l;
		loan.setId(id);
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		List<Loan> loanList = Arrays.asList(loan); // Lista do tipo correto

		Page<Loan> page = new PageImpl<>(loanList, pageRequest, loanList.size());
		
		//Page<Book> pages = new PageImpl<Book>( Arrays.asList(book), PageRequest.of(0, 10), 1);
		
		Mockito.when(repository.findByBookIsbnOrCustomer(
				Mockito.anyString(),
				Mockito.anyString(), 
				Mockito.any(PageRequest.class)))
		   .thenReturn(page);
		
		//execução
		Page<Loan> result = service.find(filterDTO, pageRequest);
		
		//verificação
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(loanList);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);

	}

	public static Loan createLoan() {

		Book book = Book.builder().id(1L).build();

		String customer = "Fulano";

		return Loan.builder().book(book).customer(customer).instante(LocalDate.now()).build();

	}
}
