package regras_negocio;
/**********************************
 * IFPB - SI
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import daodb4o.DAO;
import daodb4o.DAOMedico;
import daodb4o.DAOPaciente;
import daodb4o.DAOConsulta;
import modelo.Medico;
import modelo.Paciente;
import modelo.Consulta;

public class Fachada {
	private Fachada() {}

	private static DAOPaciente daoMedico = new DAOPaciente();
	private static DAOMedico daoPaciente = new DAOMedico();
	private static DAOConsulta daoConsulta = new DAOConsulta();

	public static void inicializar() {
		DAO.open();
	}

	public static void finalizar() {
		DAO.close();
	}

	public static Medico localizarMedico(String nome) throws Exception {
		Medico m = daoMedico.read(nome);
		if (m == null) {
			throw new Exception("Medico inexistente:" + nome);
		}
		return m;
	}
	public static Paciente localizarPaciente(String nome) throws Exception {
		Paciente a = daoPaciente.read(nome);
		if (a == null) {
			throw new Exception("Paciente inexistente:" + nome);
		}
		return a;
	}

	public static void criarMedico(String nome, String data, List<String> apelidos) throws Exception {
		DAO.begin();
		try {
			LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (DateTimeParseException e) {
			DAO.rollback();
			throw new Exception("formato data invalido:" + data);
		}
		Medico p = daoMedico.read(nome);
		if (p != null) {
			DAO.rollback();
			throw new Exception("criar Medico - nome ja existe:" + nome);
		}
		p = new Medico(nome);
		p.setDtNascimento(data);
		p.setApelidos(apelidos);
		daoMedico.create(p);
		DAO.commit();
	}

	public static void criarPaciente(String nome, String data, List<String>  apelidos, double nota) throws Exception {
		DAO.begin();
		try {
			LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (DateTimeParseException e) {
			DAO.rollback();
			throw new Exception("formato data invalido:" + data);
		}

		Medico p = daoMedico.read(nome); // nome de qualquer Medico
		if (p != null) {
			DAO.rollback();
			throw new Exception("criar Paciente - nome ja existe:" + nome);
		}

		Paciente a = new Paciente(nome, nota);
		a.setDtNascimento(data);
		a.setApelidos(apelidos);
		daoPaciente.create(a);
		DAO.commit();
	}

	public static void alterarMedico(String nome, String data, List<String> apelidos) throws Exception {
		// permite alterar data, foto e apelidos
		DAO.begin();
		Medico p = daoMedico.read(nome);
		if (p == null) {
			DAO.rollback();
			throw new Exception("alterar Medico - Medico inexistente:" + nome);
		}

		p.setApelidos(apelidos);
		if (data != null) {
			try {
				LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				p.setDtNascimento(data);
			} catch (DateTimeParseException e) {
				DAO.rollback();
				throw new Exception("alterar Medico - formato data invalido:" + data);
			}
		}

		daoMedico.update(p);
		DAO.commit();
	}

	public static void alterarPaciente(String nome, String data, List<String>  apelidos, double nota) throws Exception {
		// permite alterar data, foto e apelidos
		DAO.begin();
		Paciente a = daoPaciente.read(nome);
		if (a == null) {
			DAO.rollback();
			throw new Exception("alterar Paciente - nome inexistente:" + nome);
		}

		a.setApelidos(apelidos);
		if (data != null) {
			try {
				LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				a.setDtNascimento(data);
			} catch (DateTimeParseException e) {
				DAO.rollback();
				throw new Exception("alterar Paciente - formato data invalido:" + data);
			}
		}
		a.setNota(nota);
		daoMedico.update(a);
		DAO.commit();
	}

	public static void alterarData(String nome, String data) throws Exception {
		DAO.begin();
		Medico p = daoMedico.read(nome);
		if (p == null) {
			DAO.rollback();
			throw new Exception("alterar Medico - Medico inexistente:" + nome);
		}

		if (data != null) {
			try {
				LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				p.setDtNascimento(data);
			} catch (DateTimeParseException e) {
				DAO.rollback();
				throw new Exception("alterar data - formato data invalido:" + data);
			}
		}

		daoMedico.update(p);
		DAO.commit();
	}

	public static void alterarNome(String nome, String novonome) throws Exception {
		DAO.begin();
		Medico p = daoMedico.read(nome); // usando chave primaria
		if (p == null) {
			DAO.rollback();
			throw new Exception("alterar nome - nome inexistente:" + nome);
		}
		p.setNome(novonome);
		daoMedico.update(p);
		DAO.commit();
	}

	public static void excluirMedico(String nome) throws Exception {
		DAO.begin();
		Medico p = daoMedico.read(nome);
		if (p == null) {
			DAO.rollback();
			throw new Exception("excluir Medico - nome inexistente:" + nome);
		}

		// desligar a Medico de seus Consultas orfaos e apaga-los do banco
		for (Consulta t : p.getConsultas()) {
			daoConsulta.delete(t); // deletar o Consulta orfao
		}

		daoMedico.delete(p); // apagar a Medico
		DAO.commit();
	}

	public static void criarConsulta(String nome, String numero) throws Exception {
		DAO.begin();
		Medico p = daoMedico.read(nome);
		if (p == null) {
			DAO.rollback();
			throw new Exception("criar Consulta - nome inexistente" + nome + numero);
		}
		Consulta t = daoConsulta.read(numero);
		if (t != null) {
			DAO.rollback();
			throw new Exception("criar Consulta - numero ja cadastrado:" + numero);
		}
		if (numero.isEmpty()) {
			DAO.rollback();
			throw new Exception("criar Consulta - numero vazio:" + numero);
		}

		t = new Consulta(numero);
		p.adicionar(t);
		daoConsulta.create(t);
		DAO.commit();
	}

	public static void excluirConsulta(String numero) throws Exception {
		DAO.begin();
		Consulta t = daoConsulta.read(numero);
		if (t == null) {
			DAO.rollback();
			throw new Exception("excluir Consulta - numero inexistente:" + numero);
		}
		Medico p = t.getMedico();
		p.remover(t);
		t.setMedico(null);
		daoMedico.update(p);
		daoConsulta.delete(t);
		DAO.commit();
	}

	public static void alterarNumero(String numero, String novonumero) throws Exception {
		DAO.begin();
		Consulta t1 = daoConsulta.read(numero);
		if (t1 == null) {
			DAO.rollback();
			throw new Exception("alterar numero - numero inexistente:" + numero);
		}
		Consulta t2 = daoConsulta.read(novonumero);
		if (t2 != null) {
			DAO.rollback();
			throw new Exception("alterar numero - novo numero ja existe:" + novonumero);
		}
		if (novonumero.isEmpty()) {
			DAO.rollback();
			throw new Exception("alterar numero - novo numero vazio:");
		}

		t1.setNumero(novonumero); // substituir
		daoConsulta.update(t1);
		DAO.commit();
	}

	public static List<Medico> listarMedicos() {
		List<Medico> result = daoMedico.readAll();
		return result;
	}

	public static List<Paciente> listarPacientes() {
		List<Paciente> result = daoPaciente.readAll();
		return result;
	}

	public static List<Consulta> listarConsultas() {
		List<Consulta> result = daoConsulta.readAll();
		return result;
	}

	/**********************************************************
	 * 
	 * CONSULTAS IMPLEMENTADAS NOS DAO
	 * 
	 **********************************************************/
	public static List<Medico> consultarMedicos(String caracteres) {
		List<Medico> result;
		if (caracteres.isEmpty())
			result = daoMedico.readAll();
		else
			result = daoMedico.read(caracteres);
		return result;
	}


	public static List<Consulta> consultarConsultas(String digitos) {
		List<Consulta> result;
		if (digitos.isEmpty())
			result = daoConsulta.readAll();
		else
			result = daoConsulta.readAll(digitos);
		return result;
	}

	public static List<Medico> consultarMesNascimento(String mes) {
		List<Medico> result;
		result = daoMedico.readByMes(mes);
		return result;
	}

	public static List<Medico> consultarMedicosNConsultas(int n) {
		List<Medico> result;
		DAO.begin();
		result = daoMedico.readByNConsultas(n);
		DAO.commit();
		return result;
	}

	public static boolean temConsultaFixo(String nome) {
		return daoMedico.temConsultaFixo(nome);
	}

	public static List<Medico> consultarApelido(String ap) {
		return daoMedico.consultarApelido(ap);
	}

}
