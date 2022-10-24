package br.com.alura.forum.service.impl;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import br.com.alura.forum.service.TopicoService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnableCaching
public class TopicoServiceImpl implements TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;


    @Override
    public DetalhesDoTopicoDto detalhar(Long id) throws NotFoundException {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return new DetalhesDoTopicoDto(topico.get());
        }else{
            throw new NotFoundException("Topico não encontrado!");
        }
    }

    @Override
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public TopicoDto atualizar(Long id, AtualizacaoTopicoForm atualizacaoTopicoForm) throws NotFoundException {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            Topico topico = atualizacaoTopicoForm.atualizar(id, topicoRepository);
            return new TopicoDto(topico);
        }else{
            throw new NotFoundException("Topico não encontrado!");
        }
    }

    @Override
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public void remover(Long id) throws NotFoundException {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            topicoRepository.deleteById(id);
        }else{
            throw new NotFoundException("Topico não encontrado!");
        }

    }

    @Override
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public TopicoDto cadastrar(TopicoForm topicoForm) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        return new TopicoDto(topico);
    }

    @Override
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> listar(String nomeCurso, Pageable pageable) {
        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(pageable);
            return TopicoDto.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,pageable);
            return TopicoDto.converter(topicos);
        }

    }
}
