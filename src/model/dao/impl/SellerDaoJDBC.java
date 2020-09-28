package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error ! No rows affected");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;

		/*
		 * Aqui temos o método para remover os registro do banco de dados porém não
		 * temos nenhuma validação para o caso do usuario não digitar um id valido, da
		 * para colocar mais agora eu caguei, depois quem sabe eu retorne e arrume isso
		 */
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ? ");

			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

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
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			// Igual o findByDepartment porém sem o WHERE ai trás tudão
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");
			// como não tem o where eu removi a parte onde eu passo um departamento.,
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);

			}
			return list;
		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		} finally {

			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// Inclução do novo método de busca por departamento.
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			// só mudou a query que é executada
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId());

			rs = st.executeQuery();

			/*
			 * Aqui como a intenção é retornar uma lista com todos os vendedores de cada
			 * departamento mais sem ter que instanciar o departamento varias vezes então
			 * riamos uma lista que vai receber esses vendedores mais também já mandamos o
			 * map com o hashMap para garantir que não teremos duas instacias do mesmo
			 * departamento
			 */
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			// Aqui eu fazendo um loop na lista para saber quantos vendedores tem na lista
			while (rs.next()) {
				/*
				 * Aqui eu vou procurar pelo id do departamento para saber se tem algum
				 * departamento instanciado
				 */
				Department dep = map.get(rs.getInt("DepartmentId"));

				/*
				 * Se não tiver nenhum departamento inicializado eu instancio, caso já exista eu
				 * passo para a instancia do vendedor e boa
				 */
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);

			}
			return list;
		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		} finally {

			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
