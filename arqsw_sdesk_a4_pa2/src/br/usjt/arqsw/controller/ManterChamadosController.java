package br.usjt.arqsw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.usjt.arqsw.entity.Chamado;
import br.usjt.arqsw.entity.Fila;
import br.usjt.arqsw.service.ChamadoService;
import br.usjt.arqsw.service.FilaService;

/**
 * 
 *  @author Giovanna Selihevic 81613657 SIN3AN-MCA1
 */

@Controller
public class ManterChamadosController {
	private FilaService filaService;
	private ChamadoService chamadoService;
	
	@Autowired
	public ManterChamadosController(FilaService filaService, ChamadoService chamadoService) {
		this.filaService = filaService;
		this.chamadoService = chamadoService;
	}

	
	@RequestMapping("index")
	public String inicio() {
		return "index";
	}

	
	@RequestMapping("/tela_inicio")
	public String inicio2() {
		return "TelaPrincipal";
	}
	
	
	@Transactional
	private List<Fila> listarFilas() throws IOException{
			return filaService.listarFilas();
	}
	
	
	@Transactional
	private List<Chamado> listarChamados() throws IOException{
		return chamadoService.listarChamados();
	}
	
	
	@Transactional
	private void cadastrarChamado(String desc, Fila fila) throws IOException{
		Chamado chamado = new Chamado();
		Date d = new Date();
		chamado.setDescricao(desc);
		chamado.setFila(fila);
		chamado.setStatus("ABERTO");
		chamado.setDt_abertura(d);
		chamadoService.novoChamado(chamado);
	}
	
	
	@Transactional
	@RequestMapping("/listar_filas_exibir")
	public String listarFilasExibir(Model model) {
		try {
			model.addAttribute("filas", listarFilas());
			return "ChamadoListar";
		} catch (IOException e) {
			e.printStackTrace();
			return "Erro";
		}
	}
	
	
	@Transactional
	@RequestMapping("/cadastrar_chamado")
	public String cadastrarChamado(Model model) {
		try {
			model.addAttribute("filas", listarFilas());
			return "CadastrarChamado";
		} catch (IOException e) {
			e.printStackTrace();
			return "Erro";
		}
	}
	
	 
	@Transactional
	@RequestMapping("/chamado_cadastrado")
	public String chamadoCadastrado(String desc, Fila fila) {
		try {
			cadastrarChamado(desc, fila);
			return "ChamadoCadastrado";		
		} catch (IOException e) {
			e.printStackTrace();
			return "Erro";
		}
	}
	
	
	@Transactional
	@RequestMapping("/listar_chamados_exibir")
	public String listarChamadosExibir(@Valid Fila fila, BindingResult result, Model model) {
		try {
			if (result.hasFieldErrors("id")) {
				model.addAttribute("filas", listarFilas());
				System.out.println("Deu erro " + result.toString());
				return "ChamadoListar";
				//return "redirect:listar_filas_exibir";
			}
			fila = filaService.carregar(fila.getId());
			model.addAttribute("chamados", listarChamados());
			
			return "ChamadoListarExibir";

		} catch (IOException e) {
			e.printStackTrace();
			return "Erro";
		}
	}
	
	
	/*@RequestMapping("/fechar_chamados")
	public String fecharChamados( 
			@RequestParam Map<String, String> allRequestParams) {
		try {
			Set<String> chaves = allRequestParams.keySet();
			Iterator<String> i = chaves.iterator();
			ArrayList<Integer> lista = new ArrayList<>();
			while(i.hasNext()) {
				String chave = i.next();
				String valor = (String) allRequestParams.get(chave);
				if(valor.equals("on")) {
					lista.add(Integer.parseInt(chave));	
				}
			}
			if(lista.size()>0) {
				chamadoService.fecharChamados(lista);
			}
		}
		catch(Exception ex) {
			
		}
		return null;*/
	
	
	/*@RequestMapping("/fechar_chamados")
	public String fecharChamados(String desc, Fila fila) throws IOException {
		fecharChamados(desc, fila);
		return "FecharChamados";
		
	}*/
	
	/*@Transactional
	@RequestMapping("/fechar_chamados")
	public String fecharChamados(@RequestParam Map<String, String> allRequestParams) throws IOException {
		return "Erro";
	}*/
}
