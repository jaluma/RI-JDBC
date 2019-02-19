package uo.ri.business.transaction.management.invoice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.InvoiceGateway;

public class FacturarReparacionesBusiness {

	private static final String FACTURADA = "FACTURADA";

	private Connection connection;
	private List<Long> idsAveria;
	private InvoiceDto invoice;

	private BreakdownGateway breakdownGateway;
	private InvoiceGateway invoiceGateway;

	/**
	 * Facturar reparaciones de una lista de averias pasadas como parametro
	 * 
	 * @param idsAveria
	 *            con las averias
	 */
	public FacturarReparacionesBusiness(List<Long> idsAveria) {
		this.idsAveria = idsAveria;
	}

	/**
	 * Metodo que ejecuta una accionS
	 * 
	 * @return la factura
	 * @throws BusinessException
	 *             si falla
	 */
	public InvoiceDto execute() throws BusinessException {
		try {
			loadDb();

			verificarAveriasTerminadas(idsAveria);

			long numeroFactura = generarNuevoNumeroFactura();
			Date fechaFactura = Dates.today();
			double totalFactura = calcularImportesAverias(idsAveria);
			double iva = porcentajeIva(totalFactura, fechaFactura);
			double importe = totalFactura * (1 + iva / 100);
			importe = Round.twoCents(importe);

			long idFactura = crearFactura(numeroFactura, fechaFactura, iva, importe);
			vincularAveriasConFactura(idFactura, idsAveria);
			cambiarEstadoAverias(idsAveria, FACTURADA);

			connection.commit();

			invoice = new InvoiceDto();
			invoice.id = numeroFactura;
			invoice.date = fechaFactura;
			invoice.total = importe;
			invoice.vat = iva;
			invoice.status = FACTURADA;

		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw new BusinessException(e.getMessage());
		} catch (BusinessException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException ex) {
			}
			;
			throw e;
		} finally {
			Jdbc.close(connection);
		}

		return invoice;

	}

	/**
	 * Metodo que permite preparar la capa de Business para lanzarlo en la db
	 * 
	 * @throws SQLException
	 *             si no se puede conectar
	 */
	private void loadDb() throws SQLException {
		connection = Jdbc.getConnection();
		connection.setAutoCommit(false);

		breakdownGateway = PersistenceFactory.getBreakdownGateway();
		invoiceGateway = PersistenceFactory.getInvoiceGateway();

		breakdownGateway.setConnection(connection);
		invoiceGateway.setConnection(connection);
	}

	/**
	 * Verifica que el estado de la averia se encuentre en terminada
	 * 
	 * @param idsAveria
	 *            de la averia
	 * @throws SQLException
	 *             si falla
	 * @throws BusinessException
	 *             si no esta terminada
	 */
	private void verificarAveriasTerminadas(List<Long> idsAveria) throws SQLException, BusinessException {
		InvoiceDto invoice;

		for (Long idAveria : idsAveria) {
			invoice = breakdownGateway.findInvoiceForId(idAveria);

			if (invoice.status != null) {
				if ("ABIERTA".equalsIgnoreCase(invoice.status)) {
					throw new BusinessException("No está terminada la avería " + idAveria);
				}
				if (!"TERMINADA".equalsIgnoreCase(invoice.status)) {
					throw new BusinessException("No se ha podido añadir la avería " + idAveria);
				}
			}
		}

	}

	/**
	 * Cambia el estado de una averia al parametro pasado por parametro
	 * 
	 * @param idsAveria
	 *            de la averia
	 * @param status
	 *            a cambiar
	 * @throws SQLException
	 *             si falla
	 */
	private void cambiarEstadoAverias(List<Long> idsAveria, String status) throws SQLException {
		breakdownGateway.cambiarEstadoAverias(idsAveria, status);
	}

	/**
	 * Vincula una lista de averia con su factura
	 * 
	 * @param idFactura
	 *            a vincular
	 * @param idsAveria
	 *            a vincular
	 * @throws SQLException
	 *             si falla
	 */
	private void vincularAveriasConFactura(long idFactura, List<Long> idsAveria) throws SQLException {
		breakdownGateway.vincularAveriasConFactura(idFactura, idsAveria);
	}

	/**
	 * Metodo que crea una factura a partir de unos datos dados
	 * 
	 * @param numeroFactura
	 *            a crear
	 * @param fechaFactura
	 *            de la factura
	 * @param iva
	 *            de la factura
	 * @param totalConIva
	 *            de la factura
	 * @return cuantas se han generado
	 * @throws SQLException
	 *             si falla
	 */
	private long crearFactura(long numeroFactura, Date fechaFactura, double iva, double totalConIva)
			throws SQLException {

		InvoiceDto invoice = new InvoiceDto();
		invoice.number = numeroFactura;
		invoice.date = fechaFactura;
		invoice.vat = iva;
		invoice.total = totalConIva;

		return invoiceGateway.crearFactura(invoice);
	}

	/**
	 * Devuelve el valor del último numero de factura facturado
	 * 
	 * @return ultimo numero de la factura
	 * @throws SQLException
	 */
	private Long generarNuevoNumeroFactura() throws SQLException {
		return invoiceGateway.getUltimoNumeroFactura();
	}

	/**
	 * Metodo que determina cual es el iva a aplicar en la factura
	 * 
	 * @param totalFactura
	 *            importe total
	 * @param fechaFactura
	 *            fecha de la factura
	 * @return devuelve el porcentaje del iva
	 */
	private double porcentajeIva(double totalFactura, Date fechaFactura) {
		return Dates.fromString("1/7/2012").before(fechaFactura) ? 21.0 : 18.0;
	}

	/**
	 * Metodo que calcula el importe total de unas averias
	 * 
	 * @param idsAveria
	 *            lista con las averias
	 * @return total de la factura
	 * @throws BusinessException
	 *             si falla
	 * @throws SQLException
	 *             si falla
	 */
	protected double calcularImportesAverias(List<Long> idsAveria) throws BusinessException, SQLException {

		double totalFactura = 0.0;
		for (Long idAveria : idsAveria) {
			double importeManoObra = consultaImporteManoObra(idAveria);
			double importeRepuestos = consultaImporteRepuestos(idAveria);
			double totalAveria = importeManoObra + importeRepuestos;

			actualizarImporteAveria(idAveria, totalAveria);

			totalFactura += totalAveria;
		}
		return totalFactura;
	}

	/**
	 * Actualizar el importe de la averia
	 * 
	 * @param idAveria
	 *            a actualizar
	 * @param totalAveria
	 *            a actualizar
	 * @throws SQLException
	 *             si falla
	 */
	private void actualizarImporteAveria(Long idAveria, double totalAveria) throws SQLException {
		InvoiceDto invoice = new InvoiceDto();
		invoice.id = idAveria;
		invoice.total = totalAveria;

		invoiceGateway.actualizarImporteAveria(invoice);
	}

	/**
	 * Consultar el importe de los repuestos
	 * 
	 * @param idAveria
	 *            a consultar
	 * @return importe de los repuestos
	 * @throws SQLException
	 *             si falla
	 */
	private double consultaImporteRepuestos(Long idAveria) throws SQLException {
		BreakdownDto breakdown = new BreakdownDto();
		breakdown.id = idAveria;

		return breakdownGateway.consultaImporteRepuestos(breakdown);
	}

	/**
	 * Consultar el importe de la mano de obra
	 * 
	 * @param idAveria
	 *            a consultar
	 * @return importe
	 * @throws BusinessException
	 *             si falla
	 * @throws SQLException
	 *             si fallaS
	 */
	private double consultaImporteManoObra(Long idAveria) throws BusinessException, SQLException {
		BreakdownDto breakdown = new BreakdownDto();
		breakdown.id = idAveria;

		return breakdownGateway.consultaImporteManoObra(breakdown);
	}

}
