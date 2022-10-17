package br.com.alura.leilao.service;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class FinalizarLeilaoServiceTest {

    @Mock
    private LeilaoDao leilaoDao;
    @Mock
    private EnviadorDeEmails enviadorDeEmails;
    private FinalizarLeilaoService service;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
        this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
    }

    @Test
    void deveriaFinalizarUmLeilao() {

        List<Leilao> leiloes = criarLeilaoComLances();

        Mockito.when(this.leilaoDao.buscarLeiloesExpirados())
                .thenReturn(leiloes);

        this.service.finalizarLeiloesExpirados();

        Optional<Leilao> leilaoOptional = leiloes.stream().findFirst();

        if (!leilaoOptional.isPresent()) {
            Assertions.fail("Não existe leilão");
        }


        Leilao leilao = leilaoOptional.get();

        Assertions.assertTrue(leilao.isFechado());
        Assertions.assertEquals(leilao.getLanceVencedor().getValor(), BigDecimal.valueOf(900.0));

        Mockito.verify(leilaoDao).salvar(leilao);

    }

    @Test
    void deveriaEnviarEmailGanhadorDoLeilao() {

        List<Leilao> leiloes = criarLeilaoComLances();

        Mockito.when(this.leilaoDao.buscarLeiloesExpirados())
                .thenReturn(leiloes);

        this.service.finalizarLeiloesExpirados();

        Optional<Leilao> leilaoOptional = leiloes.stream().findFirst();

        if (!leilaoOptional.isPresent()) {
            Assertions.fail("Não existe leilão");
        }

        Leilao leilao = leilaoOptional.get();

        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(leilao.getLanceVencedor());
    }

    @Test
    void naoDeveriaEnviarEmailQuandoOcorreErroAoSalvarLeilao() {

        List<Leilao> leiloes = criarLeilaoComLances();

        Mockito.when(this.leilaoDao.buscarLeiloesExpirados())
                .thenReturn(leiloes);

        Mockito.when(leilaoDao.salvar(Mockito.any()))
                .thenThrow(RuntimeException.class);

        try {
            this.service.finalizarLeiloesExpirados();
        } catch (Exception e) {
            Mockito.verifyNoInteractions(enviadorDeEmails);
        }

    }

    private List<Leilao> criarLeilaoComLances() {

        List<Leilao> leiloes = new ArrayList<>();


        leiloes.add(LeilaoBuilder.init()
                .nome("Celular")
                .valorInicial(500.0)
                .comLance("Beltrano", 600.0)
                .comLance("Ciclano", 900.0)
                .criar()
        );

        return leiloes;
    }


}
