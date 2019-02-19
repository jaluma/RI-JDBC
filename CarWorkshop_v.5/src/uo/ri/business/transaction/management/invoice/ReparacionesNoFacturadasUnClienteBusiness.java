package uo.ri.business.transaction.management.invoice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.BreakdownGateway;

public class ReparacionesNoFacturadasUnClienteBusiness {

	/**
	 * Proceso:
	 * 
	 * - Pide el DNI del cliente
	 * 
	 * - Muestra en pantalla todas sus averias no facturadas (status <>
	 * 'FACTURADA'). De cada avería muestra su id, fecha, status, importe y
	 * descripción
	 * 
	 * @return
	 */
	private ClientDto cliente;
	private Connection connection;

	private BreakdownGateway breakdownGateway;

	/**
	 * Clase que devuelve las reparaciones no facturadas de un cliente
	 * 
	 * @param dni
	 *            con el dni del cliente
	 */
	public ReparacionesNoFacturadasUnClienteBusiness(String dni) {
		this.cliente = new ClientDto();
		this.cliente.dni = dni;
	}

	/**
	 * Metodo que ejecuta algo
	 * 
	 * @return lista de averias
	 * @throws BusinessException
	 *             si falla
	 */
	public List<BreakdownDto> execute() throws BusinessException {
		connection = null;
		try {
			loadDb();
			return breakdownGateway.reparacionesNoFacturadasUnClienteBusiness(cliente);
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
	 *             si falla
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();

		breakdownGateway = PersistenceFactory.getBreakdownGateway();

		breakdownGateway.setConnection(connection);
	}

}
