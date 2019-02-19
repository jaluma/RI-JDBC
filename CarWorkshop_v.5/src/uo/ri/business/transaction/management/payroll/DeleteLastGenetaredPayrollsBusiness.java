package uo.ri.business.transaction.management.payroll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.PayrollGateway;

public class DeleteLastGenetaredPayrollsBusiness implements Action {

	private Connection connection;
	private PayrollGateway payrollGateway;
	private int count;

	/**
	 * Clase que permite borrar las ultimas nominas generadas
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {
		try {
			loadDb();
			Date lastDayLastDate = payrollGateway.getLastDateFromPayrolls();
			Date firstDayLastDate = Dates.firstDayOfMonth(lastDayLastDate);

			count = payrollGateway.deleteLastGenetaredPayrolls(firstDayLastDate, lastDayLastDate);
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
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();
		connection.setAutoCommit(false);

		payrollGateway = PersistenceFactory.getPayrollGateway();

		payrollGateway.setConnection(connection);
	}

	/**
	 * Metodo que devuelve el numero de valores borrados
	 * 
	 * @return numero del calculo
	 */
	public int getNumberDelete() {
		return count;
	}

}
