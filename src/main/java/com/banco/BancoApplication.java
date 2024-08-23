package com.banco;

import java.math.BigDecimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.banco.application.conta.service.ContaService;
import com.banco.domain.cliente.model.Cliente;
import com.banco.domain.conta.model.Conta;

@SpringBootApplication
public class BancoApplication {

	private ContaService contaService;

	public BancoApplication(ContaService contaService) {
		this.contaService = contaService;
	}

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(BancoApplication.class, args);
		BancoApplication bancoApplication = app.getBean(BancoApplication.class);

		bancoApplication.run();
	}
	
	public void run() {
		Cliente joao = new Cliente("João");
		Cliente maria = new Cliente("Maria");

		Conta contaJoao = new Conta(joao, BigDecimal.valueOf(1000));
		Conta contaMaria = new Conta(maria, BigDecimal.valueOf(1000));

		contaService.cadastrar(contaJoao);
		contaService.cadastrar(contaMaria);

		contaService.transferir(1, 2, BigDecimal.valueOf(1000));

		System.out.println(contaService.buscar(1));
	}

}
