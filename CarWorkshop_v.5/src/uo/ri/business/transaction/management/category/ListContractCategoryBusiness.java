package uo.ri.business.transaction.management.category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicGateway;

public class ListContractCategoryBusiness implements Action {

	private Connection connection;

	private MechanicGateway mechanicGateway;
	private List<MechanicDto> list;
	private ContractCategoryDto category;

	/**
	 * Clase que permite el caculo de los mecanicos que pertenecen a una categoria
	 * 
	 * @param category
	 *            a listar
	 */
	public ListContractCategoryBusiness(ContractCategoryDto category) {
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
			list = mechanicGateway.listByCategoryId(category);
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
	 * Metodo que devuelve la lista de mecanicos
	 *
	 * @return coleccion de mecanicos
	 */
	public List<MechanicDto> getList() {
		return new ArrayList<>(list);
	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 *             si falla
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();

		mechanicGateway = PersistenceFactory.getMechanicGateway();

		mechanicGateway.setConnection(connection);
	}

}
