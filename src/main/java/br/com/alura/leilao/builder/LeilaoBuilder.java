package br.com.alura.leilao.builder;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

import java.math.BigDecimal;

public class LeilaoBuilder {

    private Leilao leilao = new Leilao("Leilao");

    public static LeilaoBuilder init() {
        return new LeilaoBuilder();
    }

    public LeilaoBuilder nome(String nome) {
        this.leilao.setNome(nome);
        return this;
    }

    public LeilaoBuilder valorInicial(Double valorInicial) {
        this.leilao.setValorInicial(BigDecimal.valueOf(valorInicial));
        return this;
    }

    public LeilaoBuilder comLance(String nomeUsuario, Double valor) {
        this.leilao.propoe(new Lance(new Usuario(nomeUsuario), BigDecimal.valueOf(valor)));
        return this;
    }

    public Leilao criar() {
        return this.leilao;
    }
}
