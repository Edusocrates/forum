package br.com.alura.forum.service;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicoService {

    DetalhesDoTopicoDto detalhar(Long id) throws NotFoundException;

    TopicoDto atualizar(Long id, AtualizacaoTopicoForm atualizacaoTopicoForm) throws NotFoundException;

    void remover(Long id) throws NotFoundException;

    TopicoDto cadastrar(TopicoForm topicoForm);

    Page<TopicoDto> listar(String nomeCurso, Pageable pageable);
}
