package uo.ri.business.transaction.management.invoice;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.InvoiceGateway;

public class FindInvoiceBussinnes {

	private InvoiceDto invoice;
	private Connection connection = null;

	private InvoiceGateway invoiceGateway;

	/**
	 * Encuentra la factura con el id que se le pasa como parametro
	 * 
	 * @param invoice
	 *            con la factura
	 */
	public FindInvoiceBussinnes(InvoiceDto invoice) {
		this.invoice = invoice;
	}

	/**
	 * Metodo que ejecuta algo
	 * 
	 * @return con la factura encontrada
	 * @throws BusinessException
	 *             si falla
	 */
	public InvoiceDto execute() throws BusinessException {
		InvoiceDto invoiceR;
		try {
			loadDb();
			invoiceR = recuperarInfoFactura();

		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException(e.getMessage());
		} finally {
			Jdbc.close(connection);
		}
		return invoiceR;
	}

	private InvoiceDto recuperarInfoFactura() throws BusinessException {
		try {
			invoice = invoiceGateway.findInvoice(invoice);
		} catch (SQLException e) {
			throw new BusinessException("No se ha podido encontrar la factura");
		}
		return invoice;
	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 *             si falla
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();

		invoiceGateway = PersistenceFactory.getInvoiceGateway();

		invoiceGateway.setConnection(connection);
	}
}
