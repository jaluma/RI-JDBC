package uo.ri.business.transaction.management.contract;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ContractGateway;

public class FindContractByMechanicIdBusiness implements Action {

	private List<ContractDto> list;
	private MechanicDto mechanic;
	private Connection connection;

	private ContractGateway contractGateway;

	/**
	 * Clase que permite encontrar un contrato pasando por parametro el id de un
	 * mecanico
	 * 
	 * @param id
	 *            a buscar
	 */
	public FindContractByMechanicIdBusiness(Long id) {
		this.mechanic = new MechanicDto();
		this.mechanic.id = id;
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
			list = contractGateway.findContractsByMechanicId(mechanic);
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

		contractGateway = PersistenceFactory.getContractGateway();

		contractGateway.setConnection(connection);
	}

	/*
	 * Metodo que devuelve la colecion de contratos encontrados
	 * 
	 * @return lista de contract
	 */
	public List<ContractDto> getList() {
		return new ArrayList<>(list);
	}

}
