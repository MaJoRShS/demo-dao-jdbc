package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		// Alteramos aqui para passar a conneção como parâmetro.
		return new SellerDaoJDBC(DB.getConnection());

	}
}
