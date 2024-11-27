/**********************************
 * IFPB - SI
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/
package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.Medico;

public class DAOMedico extends DAO<Medico>{
	
	//nome � usado como campo unico 
	public Medico read (String nome) {
		Query q = manager.query();
		q.constrain(Medico.class);
		q.descend("nome").constrain(nome);
		List<Medico> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}
	
//	public void create(Medico obj){
//		int novoid = super.gerarId(Medico.class);  	//gerar novo id da classe
//		obj.setId(novoid);				//atualizar id do objeto antes de grava-lo no banco
//		manager.store( obj );
//	}
	
}

