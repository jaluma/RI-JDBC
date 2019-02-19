package uo.ri.business.transaction.management.mecanic;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicGateway;

public class FindMechanicByIdBusiness {

	private Connection connection;
	private MechanicDto mechanic;

	private MechanicGateway mechanicGateway;

	/**
	 * Clase para encontrar los mecanicos a partir de un id
	 * 
	 * @param id
	 *            a buscar
	 */
	public FindMechanicByIdBusiness(Long id) {
		mechanic = new MechanicDto();
		mechanic.id = id;
	}

	/**
	 * Metodo que ejecuta la accion
	 * 
	 * @return mecanico
	 * @throws BusinessException
	 *             si falla
	 */
	public MechanicDto execute() throws BusinessException {
		MechanicDto mechanicR;
		try {
			loadDb();
			mechanicR = mechanicGateway.findMechanicById(mechanic);
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
		return mechanicR;
	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();

		mechanicGateway = PersistenceFactory.getMechanicGateway();

		mechanicGateway.setConnection(connection);
	}
}
