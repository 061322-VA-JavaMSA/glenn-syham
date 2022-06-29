package com.revature.daos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Product;

import com.revature.util.ConnectionUtil;

public class ProductPostgres implements ProductDAO {
	private String _table = "products";
	private static Logger log = LogManager.getLogger(ProductPostgres.class);

	@Override
	public Product createProduct(Product p) {
		String sql = "insert into " + _table + " (product_name, price) values (?,?) returning id;";
		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, p.getProduct_name());
			ps.setDouble(2, p.getPrice());
			log.info(ps);
			ResultSet rs = ps.executeQuery(); // return the id generated by the database
			if (rs.next()) {
				p.setId(rs.getInt("id"));
			}

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return p;
	}

	@Override
	public Product retrieveProductById(int id) {
		String sql = "select * from " + _table + " where id = ? ";
		Product product = null;

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			log.info(ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				product = new Product();
				returnData(rs, product);
			}
		} catch (SQLException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public List<Product> retrieveProducts() {
		// TODO Auto-generated method stub
		String sql = "select * from " + _table + ";";
		return getPreparedStatement(sql);
	}

	@Override
	public List<Product> retrieveProductsOwned(int i) {
		// TODO Auto-generated method stub
		String sql = "select * from " + _table + " where user_id is null  order by id;";
		if (i == 1) {
			sql = "select * from " + _table + " where user_id is not  null  order by id;";

		}
		return getPreparedStatement(sql);
	}

	@Override
	public boolean updateProduct(Product p) {
		String sql = "update " + _table + " set product_name = ?, price = ? where id = ?;";
		int rowsChanged = -1;

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);

			ps.setString(1, p.getProduct_name());
			ps.setDouble(2, p.getPrice());
			ps.setInt(3, p.getId());
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
	public boolean deleteProductById(int id) {
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
	public boolean setProducttoUser(int id, int uid) {
		String sql = "update " + _table + " set  user_id = ? where id = ?;";
		int rowsChanged = -1;

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, uid);
			ps.setInt(2, id);
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
	public boolean resetProduct(int id) {
		String sql = "update " + _table
				+ " set  user_id = null  where id = ?;";
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
	public List<Product> retrieveProductByUserId(int id) {
		List<Product> products = new ArrayList<>();

		String sql = "select * from " + _table + " where user_id = ?";
		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			log.info(ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// extract each field from rs for each record, map them to a Product object and
				// add them to the products arrayliost
				Product product = new Product();
				returnData(rs, product);
				products.add(product);

			}
		} catch (SQLException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public List<Product> getPreparedStatement(String sql) {
		List<Product> products = new ArrayList<>();

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				// extract each field from rs for each record, map them to a Product object and
				// add them to the products arrayliost
				Product product = new Product();
				returnData(rs, product);
				products.add(product);

			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return products;
	}

	@Override
	public Product returnData(ResultSet rs, Product product) {
		// TODO Auto-generated method stub
		try {
			product.setId(rs.getInt("id"));
			product.setProduct_name(rs.getString("product_name"));
			product.setPrice(rs.getDouble("price"));
			product.setUser_id(rs.getInt("user_id"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return product;
	}

	@Override
	public List<Product> retrieveProductByName(String n) {
		// TODO Auto-generated method stub

		String sql = "select * from " + _table + " where lower(product_name) like ?";
		List<Product> products = new ArrayList<>();

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, "%" + n.trim().toLowerCase() + "%");
			log.info(ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// extract each field from rs for each record, map them to a Product object and
				// add them to the products arrayliost
				Product product = new Product();
				returnData(rs, product);
				products.add(product);

			}
		} catch (SQLException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public Product retrieveProductByNameExact(String n) {
		String sql = "select * from " + _table + " where lower(product_name) like ?";
		Product product = null;
		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, n.trim().toLowerCase());
			log.info(ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				product = new Product();
				returnData(rs, product);
			}
		} catch (SQLException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public List<Product> retrieveProductByNameCustomer(String n) {
		// TODO Auto-generated method stub

		String sql = "select * from " + _table + " where lower(product_name) like ? and user_id is null order by id";
		List<Product> products = new ArrayList<>();

		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, "%" + n.trim().toLowerCase() + "%");
			log.info(ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// extract each field from rs for each record, map them to a Product object and
				// add them to the products arrayliost
				Product product = new Product();
				returnData(rs, product);
				products.add(product);

			}
		} catch (SQLException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public Product retrieveProductByNameExact(String n, int pid) {
		String sql = "select * from " + _table + " where lower(product_name) like ? and id != ?";
		Product product = null;
		try (Connection c = ConnectionUtil.getConnectionFromFile()) {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, n.trim().toLowerCase());
			ps.setInt(2, pid);
			log.info(ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				product = new Product();
				returnData(rs, product);
			}
		} catch (SQLException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public boolean setProductPaid(int id) {
		// TODO Auto-generated method stub
		String sql = "update " + _table + " set  paid_status = 1 where id = ?;";
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

}
