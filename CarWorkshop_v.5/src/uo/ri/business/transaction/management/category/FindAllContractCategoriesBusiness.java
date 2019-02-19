package uo.ri.business.transaction.management.category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ContractCategoryGateway;

public class FindAllContractCategoriesBusiness implements Action {

	private Connection connection;

	private ContractCategoryGateway categoryGateway;
	private List<ContractCategoryDto> list;

	/**
	 * Clase que permite encontrar todas las categorias de contratos
	 * 
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
			list = categoryGateway.listAllCategories();
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
	 * Metodo que permite obtener la lista de categorias de contratos. Ejecutar
	 * despues de execute
	 * 
	 * @return coleccion con todas las categorias
	 */
	public List<ContractCategoryDto> getList() {
		return new ArrayList<>(list);
	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();

		categoryGateway = PersistenceFactory.getCategoryGateway();

		categoryGateway.setConnection(connection);
	}
}
