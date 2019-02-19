package uo.ri.business.transaction.management.contract;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ContractGateway;

public class DeleteContractBusiness implements Action {

	private ContractDto contract;
	private Connection connection;

	private ContractGateway contractGateway;

	/**
	 * Metodo que permite borrar un contrato de la db
	 * 
	 * @param id
	 *            a borrar
	 */
	public DeleteContractBusiness(Long id) {
		this.contract = new ContractDto();
		this.contract.id = id;
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
			contractGateway.deleteContract(contract);
			connection.commit();
		} catch (SQLException | RuntimeException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException("No se ha podido borrar.");
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
		connection.setAutoCommit(false);

		contractGateway = PersistenceFactory.getContractGateway();

		contractGateway.setConnection(connection);
	}

}
