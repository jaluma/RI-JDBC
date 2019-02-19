package uo.ri.ui.cash.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.InvoiceService;
import uo.ri.conf.ServiceFactory;
import uo.ri.ui.util.Printer;

public class LiquidarFacturaAction implements Action {

	/**
	 * Proceso:
	 * 
	 * - Pedir al usuario el nº de factura - Recuperar los datos de la factura -
	 * Mostrar la factura en pantalla - Verificar que esta sin abonar (status !=
	 * 'ABONADA') - Listar en pantalla los medios de pago registrados para el
	 * cliente - Mostrar los medios de pago - Pedir el importe a cargar en cada
	 * medio de pago. Verificar que la suma de los cargos es igual al importe de la
	 * factura - Registrar los cargos en la BDD - Incrementar el acumulado de cada
	 * medio de pago - Si se han empleado bonos, decrementar el saldo disponible -
	 * Finalmente, marcar la factura como abonada
	 * 
	 */
	@SuppressWarnings("unused")
	@Override
	public void execute() throws BusinessException {
		long numberFactura = Console.readLong("Introduzca el nº de factura: ");

		InvoiceService service = ServiceFactory.getInvoiceService();

		InvoiceDto invoice = service.findInvoice(numberFactura);
		Printer.printInvoice(invoice);

		List<PaymentMeanDto> listaMetodosDePago = service.findPayMethodsForInvoice(invoice.id);
		Printer.printPaymentMeans(listaMetodosDePago);

		Map<Long, Double> cargos;
		Double importeTotal = 0.0;
		cargos = new HashMap<Long, Double>();
		Double restante = 0.0;

		while ((restante = invoice.total - importeTotal) > 0) {
			Long id;
			Double importe;
			do {
				id = Console.readLong("Introduzca la ID del medio de pago: ");
			} while (!isValidId(id, listaMetodosDePago));

			do {
				importe = Console.readDouble("Introduzca es el importe a cargar: ");
			} while (importeTotal + importe > invoice.total);

			if (cargos.containsKey(id)) {
				cargos.replace(id, cargos.get(id) + importe);
			} else {
				cargos.put(id, importe);
			}

			importeTotal += importe;
		}

		service.settleInvoice(invoice.id, cargos);

		System.out.println("Abonada correctamente.");
	}

	private boolean isValidId(Long id, List<PaymentMeanDto> listaMetodosDePago) {
		for (PaymentMeanDto paymentMeanDto : listaMetodosDePago) {
			if (paymentMeanDto.id.equals(id)) {
				return true;
			}
		}
		return false;
	}

}
