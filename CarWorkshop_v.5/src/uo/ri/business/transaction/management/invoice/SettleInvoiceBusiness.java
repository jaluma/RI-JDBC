package uo.ri.business.transaction.management.invoice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.InvoiceGateway;

public class SettleInvoiceBusiness implements Action {

	private Map<Long, Double> cargos;
	private InvoiceDto invoice;
	private Connection connection;

	private InvoiceGateway invoiceGateway;

	/**
	 * - Registrar los cargos en la BDD - Incrementar el acumulado de cada medio de
	 * pago - Si se han empleado bonos, decrementar el saldo disponible -
	 * Finalmente, marcar la factura como abonada
	 */

	/**
	 * Registra los cargos
	 * 
	 * @param idFactura
	 *            a cargas
	 * @param cargos
	 *            con los valores a guardar
	 */
	public SettleInvoiceBusiness(Long idFactura, Map<Long, Double> cargos) {
		this.cargos = cargos;
		this.invoice = new InvoiceDto();
		this.invoice.id = idFactura;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {
		try {
			loadDb();
			invoiceGateway.addCargos(invoice, cargos);
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
		} finally {
			Jdbc.close(connection);
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

		invoiceGateway = PersistenceFactory.getInvoiceGateway();

		invoiceGateway.setConnection(connection);
	}

}
