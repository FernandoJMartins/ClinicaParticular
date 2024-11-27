/**********************************
 * IFPB - SI
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/
package daodb4o;


import java.util.List;

import com.db4o.query.Candidate;
import com.db4o.query.Evaluation;
import com.db4o.query.Query;

import modelo.Paciente;

public class DAOPaciente extends DAO<Paciente>{

	//nome � usado como campo unico 
	public Paciente read (String nome) {
		Query q = manager.query();
		q.constrain(Paciente.class);
		q.descend("nome").constrain(nome);
		List<Paciente> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}

//	public void create(Paciente obj){
//		int novoid = super.gerarId(Paciente.class);  	//gerar novo id da classe
//		obj.setId(novoid);				//atualizar id do objeto antes de grava-lo no banco
//		manager.store( obj );
//	}
	/**********************************************************
	 * 
	 * TODAS AS CONSULTAS DE Paciente
	 * 
	 **********************************************************/

	public  List<Paciente> readAll(String caracteres) {
		Query q = manager.query();
		q.constrain(Paciente.class);
		q.descend("nome").constrain(caracteres).like();		//insensitive
		List<Paciente> result = q.execute(); 
		return result;
	}
	public Paciente readByCPF(String n){
		Query q = manager.query();
		q.constrain(Paciente.class);
		q.descend("cpf").descend("cpf").constrain(n);
		List<Paciente> resultados = q.execute();
		if(resultados.isEmpty())
			return null;
		else
			return resultados.getFirst();
	}

	public List<Paciente>  readByNTelefones(int n) {
		Query q = manager.query();
		q.constrain(Paciente.class);
		q.constrain(new Filtro(n));
		return q.execute(); 
	}

	public List<Paciente>  readByMes(String mes) {
		Query q = manager.query();
		q.constrain(Paciente.class);  
		q.descend("dtnascimento").constrain("/"+mes+"/").contains();
		return q.execute();
	}

	public boolean temConsultas(String nome) {
		Query q = manager.query();
		q.constrain(Paciente.class);
		q.descend("nome").constrain(nome);
		q.descend("consultas").descend("numero").constrain("3").startsWith(true);;
		return q.execute().size()>0;
	}

}

/*-------------------------------------------------*/
@SuppressWarnings("serial")
class Filtro  implements Evaluation {
	private int n;
	public Filtro (int n) {
		this.n=n;
	}
	public void evaluate(Candidate candidate) {
		Paciente p = (Paciente) candidate.getObject();
		candidate.include( p.getTelefones().size() == n );
	}
}



