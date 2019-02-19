package uo.ri.business.transaction.management.mecanic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicGateway;

public class FindActiveMechanicsBusiness {

	private Connection connection;
	private MechanicGateway mechanicGateway;

	/**
	 * Clase que lista los mecanicos activos
	 * 
	 * @return
	 * @throws BusinessException
	 */

	/**
	 * Metodo que hace algo
	 * 
	 * @return lista
	 * @throws BusinessException
	 *             si falla
	 */
	public List<MechanicDto> execute() throws BusinessException {
		List<MechanicDto> list;
		try {
			loadDb();
			list = mechanicGateway.listActive();
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
		return list;
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
