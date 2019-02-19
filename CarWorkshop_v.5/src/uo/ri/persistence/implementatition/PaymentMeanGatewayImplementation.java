package uo.ri.persistence.implementatition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.CashDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.PaymentMeanGateway;

public class PaymentMeanGatewayImplementation implements PaymentMeanGateway {

	private Connection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uo.ri.persistence.PaymentMeanGateway#findPayMethodsForInvoice(uo.ri.business.
	 * dto.InvoiceDto)
	 */
	@Override
	public List<PaymentMeanDto> findPayMethodsForInvoice(InvoiceDto invoice) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentMeanDto> lista = new ArrayList<>();

		PaymentMeanDto payment;

		try {

			ps = connection
					.prepareStatement(Conf.getInstance().getProperty("SQL_LIQUIDA_FACTURA_RECUPERAR_MEDIOS_DE_PAGO"));
			ps.setLong(1, invoice.number);

			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString(4).equals("TTarjetasCredito")) {
					payment = new CardDto();
				} else if (rs.getString(4).equals("TMetalico")) {
					payment = new CashDto();
				} else {
					payment = new VoucherDto();
				}
				payment.id = rs.getLong(1);
				payment.clientId = rs.getLong(2);
				payment.accumulated = rs.getDouble(3);
				lista.add(payment);
			}
		} finally {
			Jdbc.close(rs, ps);
		}
		return lista;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uo.ri.persistence.PaymentMeanGateway#savePayment(java.util.List)
	 */
	@Override
	public void savePayment(List<PaymentMeanDto> lista) throws SQLException {
		PreparedStatement pst = null;
		PreparedStatement pstU = null;
		ResultSet rs = null;
		try {
			pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_INSERT_SAVE_MEDIOS_DE_PAGO"));
			pstU = connection.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_SAVE_MEDIOS_DE_PAGO"));

			for (PaymentMeanDto payment : lista) {
				if (payment instanceof CashDto) {
					pst.setString(1, "TMetalico");
				} else if (payment instanceof CardDto) {
					pst.setString(1, "TTarjetasCredito");
					pst.setString(5, ((CardDto) payment).cardNumber);
					pst.setString(6, ((CardDto) payment).cardType);
					pst.setDate(7, new java.sql.Date(((CardDto) payment).cardExpiration.getTime()));
				} else {
					pst.setString(1, "TBonos");
					pst.setString(8, ((VoucherDto) payment).description);
					pst.setDouble(9, ((VoucherDto) payment).available);
				}
				pst.setLong(2, payment.id);
				pst.setDouble(3, payment.accumulated);
				pst.setLong(4, payment.clientId);

				// si salta excepcion es que existe, hay que actualizar
				try {
					pst.executeUpdate();
				} catch (SQLException e) {
					pstU.setDouble(1, payment.accumulated);
					pstU.setLong(2, payment.id);
					pstU.setLong(3, payment.clientId);
					pstU.executeUpdate();
				}
			}
		} finally {
			Jdbc.close(pstU);
			Jdbc.close(rs, pst);
		}
	}

	@Override
	public void setConnection(Connection conn) {
		connection = conn;

	}

}
