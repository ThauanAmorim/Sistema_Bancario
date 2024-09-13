package com.banco;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.banco.application.conta.service.ContaService;
import com.banco.domain.cliente.model.Cliente;
import com.banco.domain.conta.model.Conta;
import com.banco.infrastructure.utils.LogBuilder;

@SpringBootApplication
public class BancoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(BancoApplication.class);

	private Scanner scan;

	private ContaService contaService;

	public BancoApplication(ContaService contaService) {
		this.contaService = contaService;
		this.scan = new Scanner(System.in);
	}

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(BancoApplication.class, args);
		BancoApplication bancoApplication = app.getBean(BancoApplication.class);

		bancoApplication.run();
	}

	public void run() {
		try {
			menuPrincial();
		} catch (Exception exception) {
			LOGGER.error(LogBuilder.of()
					.header("Um erro aconteceu")
					.row("erro", exception.getMessage())
					.build());
		}
	}

	private void menuPrincial() {
		String opcao = "";
		do {
			clear();

			System.out.println("Menu Principal");
			System.out.println("1 - Cadastrar");
			System.out.println("2 - Depositar");
			System.out.println("3 - Sacar");
			System.out.println("4 - Transferir");
			System.out.println("5 - Alterar Ativo");
			System.out.println("6 - Buscar");
			System.out.println("q - Sair");

			System.out.print("Escolha uma opção: ");
			opcao = scan.nextLine();

			try {
				switch (opcao) {
					case "1":
						menuCadastrar();
						break;
					case "2":
						menuDepositar();
						break;
					case "3":
						menuSaque();
						break;
					case "4":
						menuTransferir();
						break;
					case "5":
						menuAlterarAtivo();
						break;
					case "6":
						menuBuscar();
						break;
					case "q":
						clear();
						System.out.println("Saindo...");
						sair();
						break;
					default:
						System.out.println("Opção inválida");
						aguardarEnter();
						break;
				}
			} catch (Exception exception) {
				LOGGER.error(LogBuilder.of()
						.header("Um erro aconteceu")
						.row("erro", exception.getMessage())
						.build(), exception);
				aguardarEnter();
			}
		} while (!opcao.equals("q"));
	}

	private void menuCadastrar() {
		clear();

		System.out.println("Menu Cadastrar");

		System.out.print("Nome: ");
		String nome = scan.nextLine();

		System.out.print("Valor inicial: ");
		BigDecimal valor = BigDecimal.valueOf(scan.nextDouble());

		Cliente cliente = new Cliente();
		cliente.setNome(nome);

		Conta conta = new Conta(cliente, valor);

		conta = contaService.cadastrar(conta);

		System.out.println("Conta Cadastrada");
		System.out.println(String.format("Número: %d, Nome: %s, Saldo: R$ %.2f", conta.getNumero(),
				conta.getCliente().getNome(), conta.getSaldo()));

		aguardarEnter();
	}

	private void menuDepositar() {
		clear();

		System.out.println("Depositar");

		System.out.print("Número da conta: ");
		int numero = scan.nextInt();

		System.out.print("Valor: ");
		BigDecimal valor = BigDecimal.valueOf(scan.nextDouble());

		Conta conta = contaService.depositar(numero, valor);

		System.out.println("Deposito realizado");
		System.out.println(String.format("Saldo: R$ %.2f", conta.getSaldo()));

		aguardarEnter();
	}

	private void menuSaque() {
		clear();

		System.out.println("Sacar");

		System.out.print("Número da conta: ");
		int numero = scan.nextInt();

		System.out.print("Valor: ");
		BigDecimal valor = BigDecimal.valueOf(scan.nextDouble());

		Conta conta = contaService.depositar(numero, valor);

		System.out.println("Saque realizado");
		System.out.println(String.format("Saldo: R$ %.2f", conta.getSaldo()));

		aguardarEnter();
	}

	private void menuTransferir() {
		clear();

		System.out.println("Transferir");

		System.out.print("Número da conta remetente: ");
		int numeroRemetente = scan.nextInt();

		System.out.print("Conta da conta do destinatário: ");
		int numeroDestinatario = scan.nextInt();

		System.out.print("Valor: ");
		BigDecimal valor = BigDecimal.valueOf(scan.nextDouble());

		Conta contaRemetente = contaService.transferir(numeroRemetente, numeroDestinatario, valor);

		System.out.println("Transferencia realizada");
		System.out.println(String.format("Saldo: R$ %.2f", contaRemetente.getSaldo()));

		aguardarEnter();
	}

	private void menuAlterarAtivo() {
		clear();

		System.out.println("Alterar ativo");

		System.out.print("Número da conta: ");
		int numero = scan.nextInt();

		Conta conta = contaService.alterarAtivo(numero);

		System.out.println("Alteração realizada");
		System.out.println(String.format("Ativo: %d", conta.isAtivo()));

		aguardarEnter();
	}

	private void menuBuscar() {
		clear();

		System.out.println("Buscar");

		System.out.print("Número da conta: ");
		int numero = scan.nextInt();

		Conta conta = contaService.buscar(numero);
		System.out.println(conta);

		aguardarEnter();
	}

	private void clear() {
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				new ProcessBuilder("clear").inheritIO().start().waitFor();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void aguardarEnter() {
		System.out.print("Pressione ENTER para continuar");
		scan = new Scanner(System.in);
		scan.nextLine();
	}

	private void sair() {
		System.exit(0);
	}

}
