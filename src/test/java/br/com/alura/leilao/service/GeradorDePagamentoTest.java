package br.com.alura.leilao.service;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.*;

class GeradorDePagamentoTest {

    @Mock
    private PagamentoDao pagamentoDao;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<Pagamento> pagamentoCaptor;


    private GeradorDePagamento geradorDePagamento;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao, clock);
    }

    @Test
    void deveriaCriarPagamentoParaOLanceVencedor() {

        LocalDate dataPagamento = createInstantClock(2022, 10, 17);


        Leilao leilao = criarLeilaoComLances();
        this.geradorDePagamento.gerarPagamento(leilao.getLanceVencedor());

        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());

        Pagamento pagamento = pagamentoCaptor.getValue();

        Assertions.assertEquals(dataPagamento.plusDays(1), pagamento.getVencimento());
        Assertions.assertEquals(leilao.getLanceVencedor().getValor(), pagamento.getValor());
        Assertions.assertFalse(pagamento.getPago());
        Assertions.assertEquals(leilao.getLanceVencedor().getUsuario(), pagamento.getUsuario());
        Assertions.assertEquals(leilao, pagamento.getLeilao());
    }

    @Test
    void deveriaCriarPagamentoComVencimentoNaSextaFeira() {

        LocalDate dataPagamento = createInstantClock(2022, 10, 14);

        Leilao leilao = criarLeilaoComLances();
        this.geradorDePagamento.gerarPagamento(leilao.getLanceVencedor());

        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());

        Pagamento pagamento = pagamentoCaptor.getValue();

        Assertions.assertEquals(dataPagamento.plusDays(3), pagamento.getVencimento());
        Assertions.assertEquals(leilao.getLanceVencedor().getValor(), pagamento.getValor());
        Assertions.assertFalse(pagamento.getPago());
        Assertions.assertEquals(leilao.getLanceVencedor().getUsuario(), pagamento.getUsuario());
        Assertions.assertEquals(leilao, pagamento.getLeilao());
    }

    @Test
    void deveriaCriarPagamentoComVencimentoNoSabado() {

        LocalDate dataPagamento = createInstantClock(2022, 10, 15);

        Leilao leilao = criarLeilaoComLances();
        this.geradorDePagamento.gerarPagamento(leilao.getLanceVencedor());

        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());

        Pagamento pagamento = pagamentoCaptor.getValue();

        Assertions.assertEquals(dataPagamento.plusDays(2), pagamento.getVencimento());
        Assertions.assertEquals(leilao.getLanceVencedor().getValor(), pagamento.getValor());
        Assertions.assertFalse(pagamento.getPago());
        Assertions.assertEquals(leilao.getLanceVencedor().getUsuario(), pagamento.getUsuario());
        Assertions.assertEquals(leilao, pagamento.getLeilao());
    }

    @Test
    void deveriaCriarPagamentoComVencimentoNoDomingo() {

        LocalDate dataPagamento = createInstantClock(2022, 10, 16);

        Leilao leilao = criarLeilaoComLances();
        this.geradorDePagamento.gerarPagamento(leilao.getLanceVencedor());

        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());

        Pagamento pagamento = pagamentoCaptor.getValue();

        Assertions.assertEquals(dataPagamento.plusDays(1), pagamento.getVencimento());
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

    private LocalDate createInstantClock(int year, int month, int dayOfMonth) {
        LocalDate data = LocalDate.of(year, month, dayOfMonth);
        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        return data;
    }
}
