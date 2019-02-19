package uo.ri.business.transaction.management.payroll;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.PayrollGateway;

public class DeleteLastPayrollForMechanicIdBusiness implements Action {

	private Connection connection;
	private PayrollGateway payrollGateway;
	private MechanicDto mechanic;

	/**
	 * Clase que borra la ultima nomina de un mecanico
	 * 
	 * @param id
	 *            a borrar
	 */
	public DeleteLastPayrollForMechanicIdBusiness(Long id) {
		mechanic = new MechanicDto();
		mechanic.id = id;
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
			payrollGateway.deleteLastPayrollForMechanicId(mechanic);
			connection.commit();
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException("No se han podido borrar.");
		} finally {
			Jdbc.close(connection);
		}

	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 *             si falla
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();
		connection.setAutoCommit(false);

		payrollGateway = PersistenceFactory.getPayrollGateway();

		payrollGateway.setConnection(connection);
	}

}
