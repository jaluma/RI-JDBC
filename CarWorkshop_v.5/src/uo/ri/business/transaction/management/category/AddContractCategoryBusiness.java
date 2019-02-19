package uo.ri.business.transaction.management.category;

import java.sql.Connection;
import java.sql.SQLException;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ContractCategoryGateway;

public class AddContractCategoryBusiness implements Action {

	private ContractCategoryDto category;
	private Connection connection;

	private ContractCategoryGateway categoryGateway;

	/**
	 * Clase que añade categorias
	 * 
	 * @param category
	 *            a añadir
	 */
	public AddContractCategoryBusiness(ContractCategoryDto category) {
		this.category = category;
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
			categoryGateway.add(category);
			connection.commit();
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException("No se ha podido insertar");
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

		categoryGateway = PersistenceFactory.getCategoryGateway();

		categoryGateway.setConnection(connection);
	}

}
