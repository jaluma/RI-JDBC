package uo.ri.persistence.implementatition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.MechanicGateway;

public class MechanicGatewayImplementation implements MechanicGateway {

	private Connection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.SQLGateway#setConnection(java.sql.Connection)
	 */
	@Override
	public void setConnection(Connection conn) {
		connection = conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.MechanicGateway#add(uo.ri.business.dto.MechanicDto)
	 */
	@Override
	public void add(MechanicDto mechanic) throws SQLException {
		// Procesar
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_INSERT_MECHANIC"));
			pst.setString(1, mechanic.dni);
			pst.setString(2, mechanic.name);
			pst.setString(3, mechanic.surname);

			pst.executeUpdate();

		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.MechanicGateway#delete(uo.ri.business.dto.MechanicDto)
	 */
	@Override
	public void delete(MechanicDto mechanic) throws SQLException, RuntimeException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_DELETE_MECHANIC"));
			pst.setLong(1, mechanic.id);

			rs = pst.executeQuery();
			rs.next();

			// si count == 0 significa que no se puede eliminar
			int count = rs.getInt(1);
			Jdbc.close(rs, pst);

			if (count == 0) {
				pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_MECHANIC"));
				pst.setLong(1, mechanic.id);

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
	 * @see uo.ri.persistence.MechanicGateway#update(uo.ri.business.dto.MechanicDto)
	 */
	@Override
	public void update(MechanicDto mechanic) throws SQLException, RuntimeException {
		// Procesar
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_MECHANIC"));
			pst.setString(1, mechanic.name);
			pst.setString(2, mechanic.surname);
			pst.setLong(3, mechanic.id);

			int count = pst.executeUpdate();

			if (count <= 0)
				throw new RuntimeException("El mecánico no existe.");

		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.MechanicGateway#list()
	 */
	@Override
	public List<MechanicDto> list() throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<MechanicDto> list = new ArrayList<MechanicDto>();
		MechanicDto mechanic;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_MECHANIC"));

			rs = pst.executeQuery();
			while (rs.next()) {
				mechanic = new MechanicDto();
				mechanic.id = rs.getLong(1);
				mechanic.dni = rs.getString(2);
				mechanic.name = rs.getString(3);
				mechanic.surname = rs.getString(4);
				list.add(mechanic);
			}
		} finally {
			Jdbc.close(rs, pst);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.MechanicGateway#listActive()
	 */
	@Override
	public List<MechanicDto> listActive() throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<MechanicDto> list = new ArrayList<MechanicDto>();
		MechanicDto mechanic;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_MECHANIC_ACTIVE"));

			rs = pst.executeQuery();
			while (rs.next()) {
				mechanic = new MechanicDto();
				mechanic.id = rs.getLong(1);
				mechanic.dni = rs.getString(2);
				mechanic.name = rs.getString(3);
				mechanic.surname = rs.getString(4);
				list.add(mechanic);
			}
		} finally {
			Jdbc.close(rs, pst);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.MechanicGateway#findMechanicById(uo.ri.business.dto.
	 * MechanicDto)
	 */
	@Override
	public MechanicDto findMechanicById(MechanicDto mechanic) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		MechanicDto mechanic1;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_MECHANIC_ACTIVE"));

			rs = pst.executeQuery();
			rs.next();
			mechanic1 = new MechanicDto();
			mechanic1.id = rs.getLong(1);
			mechanic1.dni = rs.getString(2);
			mechanic1.name = rs.getString(3);
			mechanic1.surname = rs.getString(4);
		} finally {
			Jdbc.close(rs, pst);
		}
		return mechanic1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.MechanicGateway#listByCategoryId(uo.ri.business.dto.
	 * ContractCategoryDto)
	 */
	@Override
	public List<MechanicDto> listByCategoryId(ContractCategoryDto category) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<MechanicDto> list = new ArrayList<MechanicDto>();
		MechanicDto mechanic;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_LIST_MECHANIC_BY_CATEGORY"));
			pst.setLong(1, category.id);

			rs = pst.executeQuery();
			while (rs.next()) {
				mechanic = new MechanicDto();
				mechanic.id = rs.getLong(1);
				mechanic.dni = rs.getString(2);
				mechanic.name = rs.getString(3);
				mechanic.surname = rs.getString(4);
				list.add(mechanic);
			}
		} finally {
			Jdbc.close(rs, pst);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.MechanicGateway#listActiveOrExt()
	 */
	@Override
	public List<MechanicDto> listActiveOrExt(Date payrollDate) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<MechanicDto> list = new ArrayList<MechanicDto>();
		MechanicDto mechanic;

		try {

			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_MECHANIC_ACTIVE_OR_EXT"));
			pst.setDate(1, new java.sql.Date(payrollDate.getTime()));
			pst.setDate(2, new java.sql.Date(Dates.firstDayOfMonth(payrollDate).getTime()));

			rs = pst.executeQuery();
			while (rs.next()) {
				mechanic = new MechanicDto();
				mechanic.id = rs.getLong(1);
				mechanic.dni = rs.getString(2);
				mechanic.name = rs.getString(3);
				mechanic.surname = rs.getString(4);
				list.add(mechanic);
			}
		} finally {
			Jdbc.close(rs, pst);
		}
		return list;
	}

}
