package uo.ri.business.service.implementation;

import java.util.List;
import java.util.Map;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.service.InvoiceService;
import uo.ri.business.transaction.management.invoice.FacturarReparacionesBusiness;
import uo.ri.business.transaction.management.invoice.FindInvoiceBussinnes;
import uo.ri.business.transaction.management.invoice.FindPayMethodsForInvoiceBussinnes;
import uo.ri.business.transaction.management.invoice.ReparacionesNoFacturadasUnClienteBusiness;
import uo.ri.business.transaction.management.invoice.SettleInvoiceBusiness;

public class InvoiceServiceImplementation implements InvoiceService {

	@Override
	public InvoiceDto createInvoiceFor(List<Long> idsAveria) throws BusinessException {
		FacturarReparacionesBusiness fac = new FacturarReparacionesBusiness(idsAveria);
		return fac.execute();
	}

	@Override
	public InvoiceDto findInvoice(Long numeroFactura) throws BusinessException {
		InvoiceDto invoice = new InvoiceDto();
		invoice.number = numeroFactura;
		FindInvoiceBussinnes fac = new FindInvoiceBussinnes(invoice);
		return fac.execute();
	}

	@Override
	public List<BreakdownDto> findRepairsByClient(String dni) throws BusinessException {
		ReparacionesNoFacturadasUnClienteBusiness rep = new ReparacionesNoFacturadasUnClienteBusiness(dni);
		return rep.execute();
	}

	@Override
	public List<PaymentMeanDto> findPayMethodsForInvoice(Long idFactura) throws BusinessException {
		FindPayMethodsForInvoiceBussinnes fac = new FindPayMethodsForInvoiceBussinnes(idFactura);
		return fac.execute();
	}

	@Override
	public void settleInvoice(Long idFactura, Map<Long, Double> cargos) throws BusinessException {
		SettleInvoiceBusiness car = new SettleInvoiceBusiness(idFactura, cargos);
		car.execute();
	}

}
