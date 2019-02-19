package uo.ri.business.transaction.management.mecanic;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicGateway;

public class UpdateMechanicBusiness implements Action {

	private MechanicDto mechanic;
	private Connection connection;

	private MechanicGateway mechanicGateway;

	/**
	 * 
	 * @param mechanic
	 *            a actualizar
	 */
	public UpdateMechanicBusiness(MechanicDto mechanic) {
		this.mechanic = mechanic;
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
			mechanicGateway.update(mechanic);
			connection.commit();
		} catch (SQLException | RuntimeException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException("No se ha podido actualizar correctamente.");
		} finally {
			Jdbc.close(connection);
		}
	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 *             si falla al conectarse
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();
		connection.setAutoCommit(false);

		mechanicGateway = PersistenceFactory.getMechanicGateway();

		mechanicGateway.setConnection(connection);
	}

}
