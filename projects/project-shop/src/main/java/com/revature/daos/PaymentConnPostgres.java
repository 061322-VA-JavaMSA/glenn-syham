package com.revature.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.PaymentConn;
import com.revature.util.ConnectionUtil;

public class PaymentConnPostgres implements PaymentConnDAO {
	private String _table = "payment_connection";
	private static Logger log = LogManager.getLogger(PaymentConnDAO.class);

	@Override
	public PaymentConn createPaymentConn(PaymentConn pc) {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 15;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		pc.setTransaction_no(generatedString);

		// TODO Auto-generated method stub
		String sql = "insert into " + _table
				+ " (offer_price, payment_details, user_id, product_id, offers_id, transaction_no, active) values (?,?,?,?,?,?,1) returning id;";
		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setDouble(1, pc.getOffer_price());
			ps.setString(2, pc.getPayment_details());
			ps.setInt(3, pc.getUser_id());
			ps.setInt(4, pc.getProduct_id());
			ps.setInt(5, pc.getOffers_id());
			ps.setString(6, pc.getTransaction_no());
			log.info(ps);
			ResultSet rs = ps.executeQuery(); // return the id generated by the database
			if (rs.next()) {
				pc.setId(rs.getInt("id"));
			}

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pc;
	}

	@Override
	public PaymentConn retrievePaymentConnById(int id) {
		// TODO Auto-generated method stub
		String sql = "select * from " + _table + " where id = ?;";
		PaymentConn pc = null;

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);

			ps.setInt(1, id); // this refers to the 1st ? in the sql String
			log.info(ps);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				pc = new PaymentConn();
				returnData(rs, pc);
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pc;
	}

	@Override
	public boolean updatePaymentConn(PaymentConn pc) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public boolean deletePaymentConnById(int id) {
		// TODO Auto-generated method stub
		String sql = "delete from " + _table + " where id = ?;";
		int rowsChanged = -1;
		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);

			ps.setInt(1, id);
			log.info(ps);
			rowsChanged = ps.executeUpdate();

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rowsChanged < 1) {
			return false;
		}
		return true;
	}

	@Override
	public PaymentConn returnData(ResultSet rs, PaymentConn paymentconn) {
		// TODO Auto-generated method stub
		try {
			paymentconn.setId(rs.getInt("id"));
			paymentconn.setOffer_price(rs.getDouble("offer_price"));
			paymentconn.setPayment_details(rs.getString("payment_details"));
			paymentconn.setActive(rs.getInt("active"));
			paymentconn.setUser_id(rs.getInt("user_id"));
			paymentconn.setProduct_id(rs.getInt("product_id"));
			paymentconn.setOffers_id(rs.getInt("offers_id"));
			paymentconn.setTransaction_no(rs.getString("transaction_no"));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return paymentconn;
	}

	@Override
	public PaymentConn retrievePaymentConnByProductIdUserId(int pid, int uid) {
		// TODO Auto-generated method stub
		String sql = "select * from " + _table + " where product_id = ? and user_id = ? and active = 1;";
		PaymentConn pc = null;

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);

			ps.setInt(1, pid); // this refers to the 1st ? in the sql String
			ps.setInt(2, uid); // this refers to the 1st ? in the sql String
			log.info(ps);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				pc = new PaymentConn();
				returnData(rs, pc);
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pc;
	}

	@Override
	public List<PaymentConn> retrievePaymentConnByUserId(int id) {
		// TODO Auto-generated method stub
		String sql = "select * from " + _table + " where user_id = ?;";
		List<PaymentConn> pc = new ArrayList<>();

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id); // this refers to the 1st ? in the sql String
			log.info(ps);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PaymentConn paymentconn = new PaymentConn();
				returnData(rs, paymentconn);
				pc.add(paymentconn);

			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pc;
	}

	@Override
	public List<PaymentConn> retrievePaymentConnByUserId(int id, int active) {
		// TODO Auto-generated method stub
		String sql = "select * from " + _table + " where paymentconn_id = ? and user_id = ? and active = ?;";
		List<PaymentConn> pc = new ArrayList<>();

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id); // this refers to the 1st ? in the sql String
			ps.setInt(1, active); // this refers to the 1st ? in the sql String
			log.info(ps);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PaymentConn paymentconn = new PaymentConn();
				returnData(rs, paymentconn);
				pc.add(paymentconn);

			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pc;
	}

	@Override
	public boolean resetPaymentConn(int pid, int uid) {
		String sql = "update " + _table + " set active = 0 where product_id = ? and  user_id = ? and active = 1;";
		int rowsChanged = -1;

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);

			ps.setInt(1, pid);
			ps.setInt(2, uid);
			log.info(ps);
			rowsChanged = ps.executeUpdate();

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rowsChanged < 1) {
			return false;
		}
		return true;
	}

	@Override
	public boolean paymentConnProductDelete(int pid, int uid) {
		String sql = "update " + _table
				+ " set active = 0, offers_id = null, product_id = null where product_id = ? and  user_id = ? and active = 1;";
		int rowsChanged = -1;

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);

			ps.setInt(1, pid);
			ps.setInt(2, uid);
			log.info(ps);
			rowsChanged = ps.executeUpdate();

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rowsChanged < 1) {
			return false;
		}
		return true;
	}

}
