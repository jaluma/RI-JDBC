package uo.ri.business.transaction.management.contract;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ContractGateway;

public class FindContractByIdBusiness implements Action {

	private ContractDto contract;
	private Connection connection;

	private ContractGateway contractGateway;

	/**
	 * Clase que permite encontrar un contrato a traves de un id pasado por
	 * parametro
	 * 
	 * @param id
	 *            a buscar
	 */
	public FindContractByIdBusiness(Long id) {
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
			contract = contractGateway.findContractById(contract);
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

		contractGateway = PersistenceFactory.getContractGateway();

		contractGateway.setConnection(connection);
	}

	/**
	 * Metodo que permite recuperar el contrato que se recupera de la base de datos
	 * 
	 * @return contrato buscado
	 */
	public ContractDto getContract() {
		return contract;
	}

}
