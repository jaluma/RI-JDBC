package uo.ri.persistence.implementatition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.PayrollDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.PayrollGateway;

public class PayrollGatewayImplementation implements PayrollGateway {

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
	 * @see uo.ri.persistence.PayrollGateway#findAllPayrolls()
	 */
	@Override
	public List<PayrollDto> findAllPayrolls() throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<PayrollDto> list = new ArrayList<>();

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_ALL_NOMINAS"));

			rs = pst.executeQuery();
			while (rs.next()) {
				PayrollDto payroll = new PayrollDto();
				payroll.id = rs.getLong(1);
				payroll.date = rs.getDate(2);
				payroll.baseSalary = rs.getDouble(3);
				payroll.extraSalary = rs.getDouble(4);
				payroll.productivity = rs.getDouble(5);
				payroll.triennium = rs.getDouble(6);
				payroll.irpf = rs.getDouble(7);
				payroll.socialSecurity = rs.getDouble(8);
				payroll.grossTotal = rs.getDouble(9);
				payroll.discountTotal = rs.getDouble(10);
				payroll.netTotal = rs.getDouble(11);
				list.add(payroll);
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
	 * uo.ri.persistence.PayrollGateway#findPayrollsByMechanicId(uo.ri.business.dto.
	 * MechanicDto)
	 */
	@Override
	public List<PayrollDto> findPayrollsByMechanicId(Long id) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<PayrollDto> list = new ArrayList<>();
		MechanicDto mechanic = new MechanicDto();
		mechanic.id = id;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_NOMINAS_BY_MECHANICS"));
			pst.setLong(1, mechanic.id);

			rs = pst.executeQuery();
			while (rs.next()) {
				PayrollDto payroll = new PayrollDto();
				payroll.id = rs.getLong(1);
				payroll.date = rs.getDate(2);
				payroll.baseSalary = rs.getDouble(3);
				payroll.extraSalary = rs.getDouble(4);
				payroll.productivity = rs.getDouble(5);
				payroll.triennium = rs.getDouble(6);
				payroll.irpf = rs.getDouble(7);
				payroll.socialSecurity = rs.getDouble(8);
				payroll.grossTotal = rs.getDouble(9);
				payroll.discountTotal = rs.getDouble(10);
				payroll.netTotal = rs.getDouble(11);
				list.add(payroll);
			}

		} finally {
			Jdbc.close(rs, pst);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.PayrollGateway#findPayrollById(uo.ri.business.dto.
	 * PayrollDto)
	 */
	@Override
	public PayrollDto findPayrollById(PayrollDto payrollP) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		PayrollDto payroll = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_SELECT_NOMINAS_BY_MECHANICS"));
			pst.setLong(1, payrollP.id);

			rs = pst.executeQuery();
			while (rs.next()) {
				payroll = new PayrollDto();
				payroll.id = rs.getLong(1);
				payroll.date = rs.getDate(2);
				payroll.baseSalary = rs.getDouble(3);
				payroll.extraSalary = rs.getDouble(4);
				payroll.productivity = rs.getDouble(5);
				payroll.triennium = rs.getDouble(6);
				payroll.irpf = rs.getDouble(7);
				payroll.socialSecurity = rs.getDouble(8);
				payroll.grossTotal = rs.getDouble(9);
				payroll.discountTotal = rs.getDouble(10);
				payroll.netTotal = rs.getDouble(11);
			}

		} finally {
			Jdbc.close(rs, pst);
		}
		return payroll;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.PayrollGateway#deleteLastPayrollForMechanicId(uo.ri.
	 * business.dto.MechanicDto)
	 */
	@Override
	public void deleteLastPayrollForMechanicId(MechanicDto mechanic) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_LAST_NOMINA_BY_MECHANIC"));
			pst.setLong(1, mechanic.id);

			int count = pst.executeUpdate();
			if (count <= 0)
				throw new RuntimeException("No se ha podido eliminar ningún elemento.");
		} finally {
			Jdbc.close(rs, pst);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.PayrollGateway#deleteLastGenetaredPayrolls(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public int deleteLastGenetaredPayrolls(Date startMonthDate, Date endMonthDate) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_PAYROLLS"));
			pst.setDate(1, new java.sql.Date(startMonthDate.getTime()));
			pst.setDate(2, new java.sql.Date(endMonthDate.getTime()));

			count = pst.executeUpdate();
		} finally {
			Jdbc.close(rs, pst);
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.PayrollGateway#addPayrolls(java.util.Map)
	 */
	@Override
	public int addPayrolls(Map<Long, PayrollDto> payrolls) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_ADD_PAYROLLS"));

			for (Long id : payrolls.keySet()) {
				PayrollDto payroll = payrolls.get(id);
				pst.setDate(1, new java.sql.Date(payroll.date.getTime()));
				pst.setDouble(2, payroll.baseSalary);
				pst.setDouble(3, payroll.extraSalary);
				pst.setDouble(4, payroll.productivity);
				pst.setDouble(5, payroll.triennium);
				pst.setDouble(6, payroll.irpf);
				pst.setDouble(7, payroll.socialSecurity);
				pst.setDouble(8, payroll.grossTotal);
				pst.setDouble(9, payroll.discountTotal);
				pst.setDouble(10, payroll.netTotal);
				pst.setLong(11, id);
				count += pst.executeUpdate();
			}
		} finally {
			Jdbc.close(rs, pst);
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.PayrollGateway#getLastDateFromPayrolls()
	 */
	@Override
	public Date getLastDateFromPayrolls() throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Date date = null;

		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_LAST_DATE"));

			rs = pst.executeQuery();
			while (rs.next()) {
				date = rs.getDate(1);
			}

		} finally {
			Jdbc.close(rs, pst);
		}
		return date;
	}
}
