package uo.ri.persistence;

import java.sql.SQLException;
import java.util.List;

import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;

public interface PaymentMeanGateway extends SQLGateway {

	/**
	 * Encuentra los metodos de pago de una factura
	 * 
	 * @param invoice
	 *            a buscar sus metodos de pago
	 * @return lista de metodos de pago
	 * @throws SQLException
	 *             si no hay ningun metodo de pago o falla
	 */
	List<PaymentMeanDto> findPayMethodsForInvoice(InvoiceDto invoice) throws SQLException;

	/**
	 * Guarda una serie de pagos
	 * 
	 * @param lista
	 *            con los pagos
	 * @throws SQLException
	 *             sino se guarda correctamente
	 */
	void savePayment(List<PaymentMeanDto> lista) throws SQLException;

}
