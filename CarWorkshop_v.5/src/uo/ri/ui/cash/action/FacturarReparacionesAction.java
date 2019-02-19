package uo.ri.ui.cash.action;

import java.util.ArrayList;
import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.InvoiceService;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class FacturarReparacionesAction implements Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {
		List<Long> idsAveria = new ArrayList<Long>();

		// pedir las averias a incluir en la factura
		do {
			Long id = Console.readLong("ID de averia");
			idsAveria.add(id);
		} while (masAverias());

		InvoiceService service = ServiceFactory.getInvoiceService();
		InvoiceDto invoice = service.createInvoiceFor(idsAveria);

		Printer.printInvoice(invoice);

	}

	/*
	 * Metodo que permite selecionar mas averias
	 */
	private boolean masAverias() {
		return Console.readString("¿Añadir más averias? (s/n) ").equalsIgnoreCase("s");
	}

}
