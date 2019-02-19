package uo.ri.ui.cash.action;

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.InvoiceService;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class ReparacionesNoFacturadasUnClienteAction implements Action {

	/**
	 * Proceso:
	 * 
	 * - Pide el DNI del cliente
	 * 
	 * - Muestra en pantalla todas sus averias no facturadas (status !=
	 * 'FACTURADA'). De cada avería muestra su id, fecha, status, importe y
	 * descripción
	 */
	@Override
	public void execute() throws BusinessException {
		String dni = pedirDNI();

		InvoiceService invoice = ServiceFactory.getInvoiceService();

		List<BreakdownDto> list = invoice.findRepairsByClient(dni);

		printBreakdown(list);
	}

	private String pedirDNI() {
		return Console.readString("Introduzca el DNI del cliente: ");
	}

	private void printBreakdown(List<BreakdownDto> list) {
		for (BreakdownDto breakdown : list) {
			Printer.printRepairing(breakdown);
		}
	}

}
