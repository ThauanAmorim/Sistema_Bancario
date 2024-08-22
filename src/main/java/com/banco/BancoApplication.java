package com.banco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.banco.application.conta.service.ContaService;

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
		this.contaService.criarConta();
	}

}
