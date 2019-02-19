package uo.ri.business.transaction.management.payroll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.PayrollGateway;

public class FindPayrollsByMechanicIdBusiness implements Action {

	private Connection connection;
	private PayrollGateway payrollGateway;
	private List<PayrollDto> list;
	private MechanicDto mechanic;

	/**
	 * Encontrar las nominas de un mecanico pasado como parametro
	 * 
	 * @param id
	 *            del mecanico
	 */
	public FindPayrollsByMechanicIdBusiness(Long id) {
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
			list = payrollGateway.findPayrollsByMechanicId(mechanic.id);
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
	 * Devuelve una lista de las nominas
	 * 
	 * @return list or null
	 */
	public List<PayrollDto> getList() {
		return new ArrayList<>(list);
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

}
