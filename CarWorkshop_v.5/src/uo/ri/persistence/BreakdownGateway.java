package uo.ri.persistence;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.MechanicDto;

public interface BreakdownGateway extends SQLGateway {

	/**
	 * Encuentra la factura de un id
	 * 
	 * @param id
	 *            de la factura
	 * @return InvoiceDto encontrada o null
	 * @throws SQLException
	 *             si no hay o da error
	 */
	InvoiceDto findInvoiceForId(long id) throws SQLException;

	/**
	 * Cambia el estao de una averia
	 * 
	 * @param idsAveria
	 *            de la averia
	 * @param status
	 *            a cambiar
	 * @throws SQLException
	 *             si no hay o da error
	 */
	void cambiarEstadoAverias(List<Long> idsAveria, String status) throws SQLException;

	/**
	 * Vincula una averia con una factura
	 * 
	 * @param idFactura
	 *            de la factura
	 * @param idsAveria
	 *            de la averia
	 * @throws SQLException
	 *             si no hay o da error
	 */
	void vincularAveriasConFactura(long idFactura, List<Long> idsAveria) throws SQLException;

	/**
	 * Consulta el importe de repuesto
	 * 
	 * @param breakdown
	 *            a consultar
	 * @return importe o 0.0
	 * @throws SQLException
	 *             si no hay o da error
	 */
	double consultaImporteRepuestos(BreakdownDto breakdown) throws SQLException;

	/**
	 * Consulta el importe de mano de obra
	 * 
	 * @param breakdown
	 *            a consultar
	 * @return importe o 0.0
	 * @throws SQLException
	 *             si no hay o da error
	 */
	double consultaImporteManoObra(BreakdownDto breakdown) throws SQLException;

	/**
	 * Metodo que devuelve las reparaciones no facturadas de un cliente
	 * 
	 * @param cliente
	 *            a buscar
	 * @return coleccion de reparaciones no facturadas
	 * @throws SQLException
	 *             si no hay o da error
	 */
	List<BreakdownDto> reparacionesNoFacturadasUnClienteBusiness(ClientDto cliente) throws SQLException;

	/**
	 * Obtiene el importe total de las averias
	 * 
	 * @param m
	 *            del mecanico
	 * @param startMonthDate
	 *            a buscar
	 * @param finishMonthDate
	 *            a buscar
	 * @return importe de las averias o 0
	 * @throws SQLException
	 *             si no hay o da error
	 */
	Double getImporteAveriasTotales(MechanicDto m, Date startMonthDate, Date finishMonthDate) throws SQLException;
}
