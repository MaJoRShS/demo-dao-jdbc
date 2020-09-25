package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			// Montamos a query
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			// Passamos o valor para a interrogação
			st.setInt(1, id);
			// Executa a query
			rs = st.executeQuery();

			/*
			 * Como o retorno é uma tabela precisamos montar o objeto com os valores
			 * retornados,e ai eu pego campo por campo e seto nos objetos Aqui é igual a
			 * merda do CADOC eu pego os valores de um cara e seto em outro, e como o JAVA é
			 * verboso que só o cão eu preciso dessa caralhada de coisa para setar os
			 * valores
			 */
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				// Se tudo certo eu retorno o objeto com o outro dentro para a aplicação
				return obj;

			}
			// se não tiver dado eu retorno nullo
			return null;
		} catch (SQLException e) {
			// Se der erro informo o erro
			throw new DbException(e.getMessage());
		} finally {
			/*
			 * e preciso fechar os recuros que eu usei só não fecha a conexão porque outro
			 * método pode utilizar
			 */
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	/*
	 * Aqui é a mesma coisa do de baixo, criamos uma método para aproveitar a
	 * instaciação em varios lugares, e ai tivemos que propagar o throw aqui porque
	 * pode dar erro de acesso aos dados.
	 */
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	/*
	 * Criamos métodos para instaciar o departamente para reaproveitar a
	 * instanciação e como pode dar erro no acesso aos dados do banco tivemos que
	 * propagar o SQLException que foi chamado la no método principal para cá também
	 */
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
