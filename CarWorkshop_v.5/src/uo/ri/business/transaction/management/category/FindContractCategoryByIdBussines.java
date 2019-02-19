package uo.ri.business.transaction.management.category;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ContractCategoryGateway;

public class FindContractCategoryByIdBussines implements Action {

	private Connection connection;

	private ContractCategoryGateway categoryGateway;
	private ContractCategoryDto category;

	/**
	 * Permite encontrar una categoria a través de su id
	 * 
	 * @param id
	 *            a buscar
	 */
	public FindContractCategoryByIdBussines(Long id) {
		category = new ContractCategoryDto();
		category.id = id;
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
			category = categoryGateway.findCategoryById(category.id);
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

		categoryGateway = PersistenceFactory.getCategoryGateway();

		categoryGateway.setConnection(connection);
	}

	/**
	 * Devuelve la categoria que encuentra o null si no existe ese id
	 * 
	 * @return ContractCategoryDto or null
	 */
	public ContractCategoryDto getCategory() {
		return category;
	}
}
