package uo.ri.ui.admin.action.payroll;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.service.PayrollService;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class ListPayrollAction implements Action {

	/**
	 * Clase que permite listas las nominas
	 */
	private List<PayrollDto> nominas;

	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		PayrollService payrollService = ServiceFactory.getPayrollService();

		do {
			Long idMecanico = Console.readLong("Id de mecánico: ");

			nominas = payrollService.findPayrollsByMechanicId(idMecanico);

			if (nominas.size() <= 0) {
				System.out.println("No hay nominas para este mecanico.");
				break;
			}

			do {
				printPayrollsSummary(nominas);

				Long id = Console.readLong("Número de nómina a mostrar en detalle: ");

				PayrollDto payroll = payrollService.findPayrollById(id);

				printPayrollDetail(payroll, nominas);
			} while (masNominasEnDetalle());

		} while (masNominasDeOtrosMecanicos());
	}

	/**
	 * Imprimir las nominas
	 * 
	 * @param nominas
	 */
	private void printPayrollsSummary(List<PayrollDto> nominas) {
		for (PayrollDto payroll : nominas) {
			Printer.printPayrollSummary(payroll);
		}
	}

	/**
	 * Imprime los detalles de una nomina
	 * 
	 * @param idNomina
	 * @param nominas
	 */
	private void printPayrollDetail(PayrollDto payroll, List<PayrollDto> nominas) {
		Printer.printPayrollDetail(payroll);
	}

	/**
	 * Muestra el mensaje para pedir otra nomina
	 * 
	 * @return
	 */
	private boolean masNominasEnDetalle() {
		return Console.readString("¿Mostrar otra nómina en detalle? (s/n) ").equalsIgnoreCase("s");
	}

	/**
	 * Muestra el mensaje de pedir otra lista de mecanicos
	 * 
	 * @return
	 */
	private boolean masNominasDeOtrosMecanicos() {
		return Console.readString("¿Mostrar nóminas de otro mecánico? (s/n) ").equalsIgnoreCase("s");
	}

}
