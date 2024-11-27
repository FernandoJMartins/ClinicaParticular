package modelo;

import java.util.Date;

public class Consulta {
	private int id;
	private Date data;
	private Paciente paciente;
	private Medico medico;
	private String tipo;
	
	public Consulta(int id, Date data, Paciente paciente, Medico medico, String tipo) {
		this.id = id;
		this.data = data;
		this.paciente = paciente;
		this.medico = medico;
		this.tipo = tipo;
	}
	
	
}
