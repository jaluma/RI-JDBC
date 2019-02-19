package uo.ri.persistence.implementatition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractGateway;

public class ContractGatewayImplementation implements ContractGateway {

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

	@Override
	public void addContract(ContractDto c) throws SQLException {
		PreparedStatement pst = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_ADD_CONTRACT"));
			pst.setLong(1, c.mechanicId);
			pst.setLong(2, c.typeId);
			pst.setLong(3, c.categoryId);
			pst.setDate(4, new java.sql.Date(c.startDate.getTime()));
			pst.setDate(5, new java.sql.Date(c.endDate.getTime()));
			pst.setDouble(6, c.yearBaseSalary);
			pst.setDouble(7, c.compensation);
			pst.setString(8, c.status);

			pst.executeUpdate();

		} finally {
			Jdbc.close(pst);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractGateway#updateContract(uo.ri.business.dto.
	 * ContractDto)
	 */
	@Override
	public void updateContract(ContractDto c) throws SQLException {
		PreparedStatement pst = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CONTRACT"));
			pst.setLong(1, c.mechanicId);
			pst.setLong(2, c.typeId);
			pst.setLong(3, c.categoryId);
			pst.setDate(4, new java.sql.Date(c.startDate.getTime()));
			pst.setDate(5, new java.sql.Date(c.endDate.getTime()));
			pst.setDouble(6, c.yearBaseSalary);
			pst.setDouble(7, c.compensation);
			pst.setString(8, c.status);
			pst.setLong(9, c.id);

			pst.executeUpdate();

		} finally {
			Jdbc.close(pst);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractGateway#deleteContract(uo.ri.business.dto.
	 * ContractDto)
	 */
	@Override
	public void deleteContract(ContractDto c) throws SQLException, RuntimeException {
		PreparedStatement pst = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_CONTRACT"));
			pst.setLong(1, c.id);

			int count = pst.executeUpdate();

			if (count <= 0) {
				throw new RuntimeException();
			}

		} finally {
			Jdbc.close(pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractGateway#finishContract(uo.ri.business.dto.
	 * ContractDto, java.util.Date)
	 */
	@Override
	public void finishContract(ContractDto dto, Date endDate) throws SQLException {
		PreparedStatement pst = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_FINISH_CONTRACT"));
			pst.setDate(1, new java.sql.Date(endDate.getTime()));
			pst.setLong(2, dto.id);

			pst.executeUpdate();

		} finally {
			Jdbc.close(pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.ContractGateway#findContractById(uo.ri.business.dto.
	 * ContractDto)
	 */
	@Override
	public ContractDto findContractById(ContractDto dto) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		ContractDto contract = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_CONTRACT_BY_ID"));
			pst.setLong(1, dto.id);

			rs = pst.executeQuery();
			while (rs.next()) {
				contract = new ContractDto();
				contract.id = rs.getLong(1);
				contract.mechanicId = rs.getLong(2);
				contract.typeId = rs.getLong(3);
				contract.categoryId = rs.getLong(4);
				contract.startDate = rs.getDate(5);
				contract.endDate = rs.getDate(6);
				contract.yearBaseSalary = rs.getDouble(7);
				contract.compensation = rs.getDouble(8);
				contract.status = rs.getString(9);
				contract.dni = rs.getString(10);
				contract.categoryName = rs.getString(11);
				contract.typeName = rs.getString(12);
			}

		} finally {
			Jdbc.close(rs, pst);
		}
		return contract;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.ContractGateway#findContractsByMechanicId(uo.ri.business.
	 * dto.MechanicDto)
	 */
	@Override
	public List<ContractDto> findContractsByMechanicId(MechanicDto mechanic) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ContractDto> list = new ArrayList<>();

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_CONTRACT_BY_MECHANIC_ID"));
			pst.setLong(1, mechanic.id);

			rs = pst.executeQuery();
			while (rs.next()) {
				ContractDto contract = new ContractDto();
				contract.id = rs.getLong(1);
				contract.mechanicId = rs.getLong(2);
				contract.typeId = rs.getLong(3);
				contract.categoryId = rs.getLong(4);
				contract.startDate = rs.getDate(5);
				contract.endDate = rs.getDate(6);
				contract.yearBaseSalary = rs.getDouble(7);
				contract.compensation = rs.getDouble(8);
				contract.status = rs.getString(9);
				contract.dni = rs.getString(10);
				contract.categoryName = rs.getString(11);
				contract.typeName = rs.getString(12);
				list.add(contract);
			}

		} finally {
			Jdbc.close(rs, pst);
		}
		return list;
	}

}
