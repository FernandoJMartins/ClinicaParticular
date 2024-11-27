package appconsole;
/**********************************
 * IFPB - SI
 * Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/


import modelo.Medico;
import modelo.Paciente;
import modelo.Consulta;
import regras_negocio.Fachada;

public class Listar {

	public Listar(){
		try {
			Fachada.inicializar();

			System.out.println("*** Listagem de Pacientes:");
			for(Paciente p : Fachada.listarPacientes())		
				System.out.println(p);

			System.out.println("\n*** Listagem de Medicos:");
			for(Medico a : Fachada.listarMedicos())		
				System.out.println(a);

			System.out.println("\n*** Listagem de Consultas:");
			for(Consulta t : Fachada.listarConsultas())	
				System.out.println(t);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Fachada.finalizar();
	}


	//=================================================
	public static void main(String[] args) {
		new Listar();
	}
}

