package uo.ri.persistence;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.PayrollDto;

public interface PayrollGateway extends SQLGateway {

	/**
	 * Encuentra todas las nominas
	 * 
	 * @return collecion de nominas
	 * @throws SQLException
	 *             si falla
	 */
	List<PayrollDto> findAllPayrolls() throws SQLException;

	/**
	 * @param id
	 *            of the mechanic
	 * 
	 * @return a list with PayrollDto of the mechanic, or an empty list if the
	 *         mechanic has none
	 * @throws SQLException
	 *             si falla
	 */
	List<PayrollDto> findPayrollsByMechanicId(Long id) throws SQLException;

	/**
	 * @param payroll
	 *            of the payroll
	 * 
	 * @return the PayrtolDto object for payroll, or null if does not exist
	 * @throws SQLException
	 *             si falla
	 */
	PayrollDto findPayrollById(PayrollDto payroll) throws SQLException;

	/**
	 * @param mechanic
	 *            a borrar
	 * @throws SQLException
	 *             if there are no payroll with that id
	 */
	void deleteLastPayrollForMechanicId(MechanicDto mechanic) throws SQLException;

	/**
	 * Deletes all the payrolls generated the same day as the most recent payroll
	 * registered in the system
	 * 
	 * @param startMonthDate
	 *            a borrar
	 * @param endMonthDate
	 *            a borrar
	 * @return the number of deleted payrolls, might be 0
	 * @throws SQLException
	 *             si falla
	 */
	int deleteLastGenetaredPayrolls(Date startMonthDate, Date endMonthDate) throws SQLException;

	/**
	 * Añade unas nominas
	 * 
	 * @param payrolls
	 *            lista de nominas
	 * @return int cuantas se han generado
	 * @throws SQLException
	 *             si falla
	 */
	int addPayrolls(Map<Long, PayrollDto> payrolls) throws SQLException;

	/**
	 * Obtiene la ultima fecha de una nomina
	 * 
	 * @return Date de la ultima nomina
	 * @throws SQLException
	 *             si falla
	 */
	Date getLastDateFromPayrolls() throws SQLException;

}
