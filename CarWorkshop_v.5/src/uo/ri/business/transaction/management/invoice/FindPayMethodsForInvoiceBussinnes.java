package uo.ri.business.transaction.management.invoice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.InvoiceGateway;
import uo.ri.persistence.PaymentMeanGateway;

public class FindPayMethodsForInvoiceBussinnes {

	private InvoiceDto invoice;
	private Connection connection;

	private PaymentMeanGateway paymentMeanGateway;
	private InvoiceGateway invoiceGateway;

	/**
	 * Encontrar los metodos de pago de una factura
	 * 
	 * @param idFactura
	 *            con la factura
	 */
	public FindPayMethodsForInvoiceBussinnes(Long idFactura) {
		this.invoice = new InvoiceDto();
		invoice.id = idFactura;
	}

	/**
	 * Metodo que prueba algo
	 * 
	 * @return lista de medios de pago
	 * @throws BusinessException
	 *             si falla
	 */
	public List<PaymentMeanDto> execute() throws BusinessException {
		List<PaymentMeanDto> list;
		try {
			loadDb();
			// comprobación de que esta abonada
			checkStatus();

			list = recuperarMediosDePago();
			connection.commit();
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException(e.getMessage());
		} catch (BusinessException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
			}
			;
			throw e;
		} finally {
			Jdbc.close(connection);
		}
		return list;
	}

	/**
	 * Metodo que devuelve los medios de pago que se calcula en execute()
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List<PaymentMeanDto> recuperarMediosDePago() throws SQLException {
		return paymentMeanGateway.findPayMethodsForInvoice(invoice);
	}

	/**
	 * Comprueba que el estaod de la factura no este abonada
	 * 
	 * @throws BusinessException
	 * @throws SQLException
	 */
	private void checkStatus() throws BusinessException, SQLException {
		invoice = invoiceGateway.findInvoicebyId(invoice);

		switch (invoice.status) {
		case "ABONADA":
			throw new BusinessException("La factura ya esta abonada");
		}
	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();
		connection.setAutoCommit(false);

		paymentMeanGateway = PersistenceFactory.getPaymentMeanGateway();
		invoiceGateway = PersistenceFactory.getInvoiceGateway();

		paymentMeanGateway.setConnection(connection);
		invoiceGateway.setConnection(connection);
	}
}
