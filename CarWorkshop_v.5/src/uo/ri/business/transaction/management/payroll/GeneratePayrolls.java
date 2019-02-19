package uo.ri.business.transaction.management.payroll;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.menu.Action;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.PayrollGateway;

public class GeneratePayrolls implements Action {

	private static final double NUM_PAGAS = 14;

	private Connection connection;
	private PayrollGateway payrollGateway;
	private ContractGateway contractGateway;
	private MechanicGateway mechanicGateway;
	private ContractCategoryGateway categoryGateway;
	private BreakdownGateway breakdownGateway;

	private int count;

	/**
	 * Clase que genera las nominas del mes pasado
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

			Date payrollDate = Dates.trunc(Dates.lastDayOfMonth(Dates.subMonths(Dates.today(), 1)));

			List<MechanicDto> listMechanicActives = mechanicGateway.listActiveOrExt(payrollDate);
			Map<Long, PayrollDto> payrolls = new HashMap<>();

			for (MechanicDto m : listMechanicActives) {

				ContractDto contract = null;
				ContractCategoryDto category = null;

				contract = getContractActive(m, payrollDate);

				category = categoryGateway.findCategoryById(contract.categoryId);

				PayrollDto payroll = new PayrollDto();
				payroll.date = payrollDate;
				payroll.baseSalary = contract.yearBaseSalary / NUM_PAGAS;
				payroll.extraSalary = getExtraSalary(payroll.baseSalary, payrollDate);
				payroll.productivity = category.productivityPlus * breakdownGateway.getImporteAveriasTotales(m,
						Dates.firstDayOfMonth(Dates.subMonths(Dates.today(), 1)), payrollDate);
				payroll.triennium = getTrienniumSalary(contract.startDate, payrollDate, category.trieniumSalary);

				payroll.grossTotal = payroll.baseSalary + payroll.extraSalary + payroll.productivity
						+ payroll.triennium;

				payroll.irpf = getIrpf(contract.yearBaseSalary, payroll.grossTotal);
				payroll.socialSecurity = contract.yearBaseSalary / 12 * 0.05;

				payroll.discountTotal = payroll.irpf + payroll.socialSecurity;
				payroll.netTotal = payroll.grossTotal - payroll.discountTotal;

				if (!checkSiYaExiste(payrollDate, contract)) {
					payrolls.put(contract.id, payroll);
				}

			}

			count = payrollGateway.addPayrolls(payrolls);

			connection.commit();
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
	 * Metodo que devuelve el contrato que se encuentra en vigor
	 * 
	 * @param m
	 * @param payrollDate
	 * @return
	 * @throws BusinessException
	 * @throws SQLException
	 */
	private ContractDto getContractActive(MechanicDto m, Date payrollDate) throws BusinessException, SQLException {
		List<ContractDto> contracts = contractGateway.findContractsByMechanicId(m);
		for (ContractDto contract : contracts) {
			if (contract.status.equals("ACTIVE")
					|| contract.endDate != null && Dates.isAfter(payrollDate, contract.endDate)
							&& Dates.isBefore(Dates.firstDayOfMonth(payrollDate), contract.endDate)
					|| contract.endDate != null && Dates.trunc(contract.endDate).equals(Dates.trunc(payrollDate))) {
				// if (contract.endDate == null && Dates.isAfter(contract.startDate,
				// payrollDate) && contract.status.equals("ACTIVE") ||
				// Dates.isAfter(contract.startDate, payrollDate) &&
				// Dates.isBefore(contract.endDate, payrollDate) &&
				// contract.status.equals("ACTIVE") ) {
				return contract;
			}
		}
		throw new BusinessException("No tiene contrato en vigor");
	}

	/**
	 * Metodo que obtiene el salario extra
	 * 
	 * @param baseSalary
	 * @param payrollDate
	 * @return
	 */
	private double getExtraSalary(double baseSalary, Date payrollDate) {
		double extraSalary = 0;
		int month = Dates.month(payrollDate);
		if (month == Month.JUNE.getValue() || month == Month.DECEMBER.getValue()) {
			extraSalary = baseSalary;
		}
		return extraSalary;
	}

	/**
	 * Obtiene el valor del irpf
	 * 
	 * @param yearSalary
	 * @return
	 */
	private double getIrpf(double yearSalary, double grossTotal) {
		if (yearSalary < 12000) {
			return 0;
		} else if (yearSalary < 30000) {
			return grossTotal * 0.1;
		} else if (yearSalary < 40000) {
			return grossTotal * 0.15;
		} else if (yearSalary < 50000) {
			return grossTotal * 0.2;
		} else if (yearSalary < 60000) {
			return grossTotal * 0.3;
		} else {
			return grossTotal * 0.4;
		}
	}

	/**
	 * Devuelve el valor del salario por trienio
	 * 
	 * @param startDate
	 * @param payrollDate
	 * @param trieniumSalary
	 * @return
	 */
	private double getTrienniumSalary(Date startDate, Date payrollDate, double trieniumSalary) {
		long yearsOld = Dates.diffDays(payrollDate, startDate) / 365;
		return yearsOld / 3 * trieniumSalary;
	}

	private boolean checkSiYaExiste(Date payrollDate, ContractDto contract) throws SQLException {
		List<PayrollDto> list = payrollGateway.findPayrollsByMechanicId(contract.mechanicId);

		for (PayrollDto payroll : list) {
			if (payroll.date.equals(payrollDate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();
		connection.setAutoCommit(false);

		payrollGateway = PersistenceFactory.getPayrollGateway();
		contractGateway = PersistenceFactory.getContractGateway();
		mechanicGateway = PersistenceFactory.getMechanicGateway();
		categoryGateway = PersistenceFactory.getCategoryGateway();
		breakdownGateway = PersistenceFactory.getBreakdownGateway();

		payrollGateway.setConnection(connection);
		contractGateway.setConnection(connection);
		mechanicGateway.setConnection(connection);
		categoryGateway.setConnection(connection);
		breakdownGateway.setConnection(connection);
	}

	/**
	 * Metodo que devuelve el numero de nominas generadas
	 * 
	 * @return numero calculado
	 */
	public int getNumberGenerated() {
		return count;
	}

}
