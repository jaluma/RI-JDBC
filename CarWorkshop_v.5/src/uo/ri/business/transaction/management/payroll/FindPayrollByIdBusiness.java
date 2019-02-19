package uo.ri.business.transaction.management.payroll;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.PayrollGateway;

public class FindPayrollByIdBusiness implements Action {

	private Connection connection;
	private PayrollGateway payrollGateway;
	private PayrollDto payroll;

	/**
	 * Clase que encuentra la nomina por una id
	 * 
	 * @param id
	 *            de la nomina
	 */
	public FindPayrollByIdBusiness(Long id) {
		payroll = new PayrollDto();
		payroll.id = id;
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
			payroll = payrollGateway.findPayrollById(payroll);
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

		payrollGateway = PersistenceFactory.getPayrollGateway();

		payrollGateway.setConnection(connection);
	}

	/**
	 * Devuelve una nomina
	 * 
	 * @return nomina or null
	 */
	public PayrollDto getPayroll() {
		return payroll;
	}

}
