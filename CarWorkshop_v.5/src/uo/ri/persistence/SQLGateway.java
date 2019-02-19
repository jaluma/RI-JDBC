package uo.ri.persistence;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLGateway {

	/**
	 * Configura la conexion al gateway
	 * 
	 * @param conn
	 *            de la conexion
	 * @throws SQLException
	 *             si no se puede conectar
	 */
	void setConnection(Connection conn) throws SQLException;
}
