package com.blink.backend.controller.dashboard;


import lombok.Getter;

import java.math.BigDecimal;

//@Setter
@Getter
public class DashboardDTO {

    private Long totalNovasMensagens;
    private Long totalAgendamentosRealizados;
    private Long totalComparecimentos;
    private Long totalVendas;
    private BigDecimal totalValorVendas;


    public DashboardDTO(Long totalNovasMensagens, Long totalAgendamentosRealizados,
                        Long totalComparecimentos, Long totalVendas, BigDecimal totalValorVendas) {

        this.totalNovasMensagens = totalNovasMensagens;
        this.totalAgendamentosRealizados = totalAgendamentosRealizados;
        this.totalComparecimentos = totalComparecimentos;
        this.totalVendas = totalVendas;
        this.totalValorVendas = totalValorVendas;

    }

}
