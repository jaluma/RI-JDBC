package uo.ri.business.transaction.management.mecanic;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicGateway;

public class DeleteMechanicBusiness implements Action {

	private MechanicDto mechanic;
	private Connection connection;

	private MechanicGateway mechanicGateway;

	/**
	 * Clase que borra un mecanico
	 * 
	 * @param idMecanico
	 *            a borrar
	 */
	public DeleteMechanicBusiness(Long idMecanico) {
		mechanic = new MechanicDto();
		mechanic.id = idMecanico;
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
			mechanicGateway.delete(mechanic);

			connection.commit();
		} catch (SQLException | RuntimeException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException("No se ha podido borrar.");
		} finally {
			Jdbc.close(connection);
		}
	}

	/**
	 * Conecta a la db
	 * 
	 * @throws SQLException
	 *             si falla
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();
		connection.setAutoCommit(false);

		mechanicGateway = PersistenceFactory.getMechanicGateway();

		mechanicGateway.setConnection(connection);
	}

}
