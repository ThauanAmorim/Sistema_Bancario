package com.banco.infrastructure.schedule.conta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.banco.application.conta.service.ContaService;
import com.banco.infrastructure.utils.LogBuilder;

@Component
public class ContaSchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContaSchedule.class);

    private final ContaService contaService;

    public ContaSchedule(final ContaService contaService) {
        this.contaService = contaService;
    }
    
    @Scheduled(cron = "0 0 2 1 * *")
    // @Scheduled(cron = "0/5 * * * * *")
    public void redaFixa() {
        LOGGER.info(LogBuilder.of()
                .header("INICIANDO RENDA FIXA")
                .build());

        contaService.rendaFixa();

        LOGGER.info(LogBuilder.of()
                .header("FINALIZANDO RENDA FIXA")
                .build());
    }

}
