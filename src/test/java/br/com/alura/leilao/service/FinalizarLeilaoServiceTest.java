package br.com.alura.leilao.service;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class FinalizarLeilaoServiceTest {

    @Mock
    private LeilaoDao leilaoDao;
    private FinalizarLeilaoService service;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
        this.service = new FinalizarLeilaoService(leilaoDao);
    }

    @Test
    void deveriaFinalizarUmLeilao() {
        this.service.finalizarLeiloesExpirados();

        // cenario
        // acao
        // validacao
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
