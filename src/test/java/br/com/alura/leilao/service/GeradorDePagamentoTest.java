package br.com.alura.leilao.service;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

class GeradorDePagamentoTest {

    @Mock
    private PagamentoDao pagamentoDao;

    @Captor
    private ArgumentCaptor<Pagamento> pagamentoCaptor;


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

        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());

        Pagamento pagamento = pagamentoCaptor.getValue();

        Assertions.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        Assertions.assertEquals(leilao.getLanceVencedor().getValor(), pagamento.getValor());
        Assertions.assertFalse(pagamento.getPago());
        Assertions.assertEquals(leilao.getLanceVencedor().getUsuario(), pagamento.getUsuario());
        Assertions.assertEquals(leilao, pagamento.getLeilao());
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
