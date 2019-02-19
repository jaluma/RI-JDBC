package uo.ri.business.transaction.management.contract;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ContractGateway;

public class FinishContractBusiness implements Action {

	private ContractDto contract;
	private Date endDate;
	private Connection connection;

	private ContractGateway contractGateway;

	/**
	 * Clase que permite finalizar un contrato activo
	 * 
	 * @param id
	 *            del contrato
	 * @param end
	 *            fecha de fin de contrato
	 */
	public FinishContractBusiness(Long id, Date end) {
		this.contract = new ContractDto();
		this.contract.id = id;
		this.endDate = end;
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
			contractGateway.finishContract(contract, endDate);
			connection.commit();
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
		connection.setAutoCommit(false);

		contractGateway = PersistenceFactory.getContractGateway();

		contractGateway.setConnection(connection);
	}

}
