package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import br.com.alura.forum.service.TopicoService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {


	@Autowired
	private TopicoService topicoService;


	@GetMapping
//	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
								 @PageableDefault(sort = "id",
										 direction = Sort.Direction.DESC,
										 page = 0, size = 10) Pageable paginacao) {

		Page<TopicoDto> lista = topicoService.listar(nomeCurso,paginacao);
		return lista;
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		TopicoDto topicoDto = topicoService.cadastrar(form);
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoDto.getId()).toUri();
		return ResponseEntity.created(uri).body(topicoDto);

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) throws NotFoundException {
		//Optional<Topico> topico = topicoRepository.findById(id);
		DetalhesDoTopicoDto detalhesDoTopicoDto = topicoService.detalhar(id);
		return ResponseEntity.ok(detalhesDoTopicoDto);
	}
	
	@PutMapping("/{id}")
	@Transactional
//	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) throws NotFoundException {
		TopicoDto topicoDto = topicoService.atualizar(id,form);
		return ResponseEntity.ok(topicoDto);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
//	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) throws NotFoundException {
		topicoService.remover(id);
		return ResponseEntity.ok().build();
	}

}







