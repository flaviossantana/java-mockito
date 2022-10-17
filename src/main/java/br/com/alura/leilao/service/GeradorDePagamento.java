package br.com.alura.leilao.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentoDao;

	private Clock clock;

	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentoDao, Clock clock) {
		this.pagamentoDao = pagamentoDao;
		this.clock = clock;
	}
	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now(clock).plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
		this.pagamentoDao.salvar(pagamento);
	}

	private LocalDate proximoDiaUtil(LocalDate data) {
		DayOfWeek dayOfWeek = data.getDayOfWeek();

		if(dayOfWeek == DayOfWeek.SATURDAY) {
			return data.plusDays(2);
		} else if(dayOfWeek == DayOfWeek.SUNDAY) {
			return data.plusDays(1);
		}

		return data;
	}

}
