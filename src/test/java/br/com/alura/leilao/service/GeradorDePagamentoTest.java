package br.com.alura.leilao.service;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GeradorDePagamentoTest {

    @Mock
    private PagamentoDao pagamentoDao;

    private GeradorDePagamento geradorDePagamento;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void deveriaCriarPagamentoParaOLanceVencedor() {
        Leilao leilao = criarLeilaoComLances();
        this.geradorDePagamento.gerarPagamento(leilao.getLanceVencedor());
    }

    private Leilao criarLeilaoComLances() {
        return LeilaoBuilder.init()
                .nome("Celular")
                .valorInicial(500.0)
                .comLance("Beltrano", 600.0)
                .lanceVencedor("Ciclano", 900.0)
                .fechado()
                .criar();
    }
}
