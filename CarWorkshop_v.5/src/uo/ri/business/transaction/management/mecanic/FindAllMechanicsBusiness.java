package uo.ri.business.transaction.management.mecanic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicGateway;

public class FindAllMechanicsBusiness {

	private Connection connection;

	private MechanicGateway mechanicGateway;

	/**
	 * Encontrar todos los mecanicos
	 * 
	 * @return
	 * @throws BusinessException
	 */

	/**
	 * Metodo que hace algo
	 * 
	 * @return lista de mecanicos
	 * @throws BusinessException
	 *             si falla
	 */
	public List<MechanicDto> execute() throws BusinessException {
		List<MechanicDto> list;
		try {
			loadDb();
			list = mechanicGateway.list();
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
