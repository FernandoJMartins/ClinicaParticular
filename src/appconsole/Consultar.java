package appconsole;
/**********************************
 * IFPB - SI
 * Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/

import modelo.Medico;
import modelo.Consulta;
import regras_negocio.Fachada;


public class Consultar {

	public Consultar(){

		try {
			Fachada.inicializar();
			System.out.println("\nMedicos com nome jo ");
			for(Medico p : Fachada.localizarMedico("jo")) 
				System.out.println(p);

			System.out.println("\nConsultas com numero 987 ");
			for(Consulta t : Fachada.consultarConsultas("987")) 
				System.out.println(t);

			System.out.println("\nMedicos que nasceram no mes 02");
			for(Medico p : Fachada.consultarMesNascimento("02")) 
				System.out.println(p);

			System.out.println("\nMedicos com apelido mary");
			for(Medico p : Fachada.consultarApelido("mary")) 
				System.out.println(p);


			System.out.println("\nMedicos com dois Consultas " );
			for(Medico p : Fachada.consultarMedicosNConsultas(2) ) 
				System.out.println(p);

			System.out.println("\nmaria tem Consulta fixo?\n"+
					Fachada.temConsultaFixo("maria") );



		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		Fachada.finalizar();
		System.out.println("\nfim do programa");
	}


	//=================================================
	public static void main(String[] args) {
		new Consultar();
	}
}

