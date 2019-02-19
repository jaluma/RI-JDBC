package uo.ri.persistence.implementatition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractCategoryGateway;

public class ContractCategoryGatewayImplementation implements ContractCategoryGateway {

	private Connection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.SQLGateway#setConnection(java.sql.Connection)
	 */
	@Override
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractCategoryGateway#add(uo.ri.business.dto.
	 * ContractCategoryDto)
	 */
	@Override
	public void add(ContractCategoryDto category) throws SQLException {
		// Procesar
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_INSERT_CATEGORY"));
			pst.setString(1, category.name);
			pst.setDouble(2, category.trieniumSalary);
			pst.setDouble(3, category.productivityPlus);

			pst.executeUpdate();

		} finally {
			Jdbc.close(rs, pst);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractCategoryGateway#delete(uo.ri.business.dto.
	 * ContractCategoryDto)
	 */
	@Override
	public void delete(ContractCategoryDto category) throws SQLException, RuntimeException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_DELETE_CATEGORY"));
			pst.setLong(1, category.id);

			rs = pst.executeQuery();
			rs.next();

			// si count == 0 significa que no se puede eliminar
			int count = rs.getInt(1);
			Jdbc.close(rs, pst);

			if (count == 0) {
				pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_CATEGORY"));
				pst.setLong(1, category.id);

				count = pst.executeUpdate();
				if (count <= 0)
					throw new RuntimeException("No se ha podido eliminar correctamente.");
			} else {
				throw new RuntimeException("No se ha podido eliminar correctamente.");
			}
		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractCategoryGateway#update(uo.ri.business.dto.
	 * ContractCategoryDto)
	 */
	@Override
	public void update(ContractCategoryDto category) throws SQLException, RuntimeException {
		// Procesar
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CATEGORY"));
			pst.setString(1, category.name);
			pst.setDouble(2, category.trieniumSalary);
			pst.setDouble(3, category.productivityPlus);
			pst.setLong(4, category.id);

			int count = pst.executeUpdate();

			if (count <= 0)
				throw new RuntimeException("No se ha podido eliminar correctamente.");

		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractCategoryGateway#listAllCategories()
	 */
	@Override
	public List<ContractCategoryDto> listAllCategories() throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContractCategoryDto> list = new ArrayList<ContractCategoryDto>();
		ContractCategoryDto category;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_LIST_CATEGORY"));

			rs = pst.executeQuery();
			while (rs.next()) {
				category = new ContractCategoryDto();
				category.id = rs.getLong(1);
				category.name = rs.getString(2);
				category.trieniumSalary = rs.getDouble(3);
				category.productivityPlus = rs.getDouble(4);
				list.add(category);
			}
		} finally {
			Jdbc.close(rs, pst);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.ContractCategoryGateway#findCategoryById(java.lang.Long)
	 */
	@Override
	public ContractCategoryDto findCategoryById(Long categoryId) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		ContractCategoryDto category1 = null;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_CATEGORY"));
			pst.setLong(1, categoryId);

			rs = pst.executeQuery();
			while (rs.next()) {
				category1 = new ContractCategoryDto();
				category1.id = rs.getLong(1);
				category1.name = rs.getString(2);
				category1.trieniumSalary = rs.getDouble(3);
				category1.productivityPlus = rs.getDouble(4);
			}
		} finally {
			Jdbc.close(rs, pst);
		}
		return category1;
	}

}
