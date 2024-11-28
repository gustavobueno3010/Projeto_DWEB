package com.fatec.controle_financeiro.controllers;

import com.fatec.controle_financeiro.domain.categoria.CategoriaRepository;
import com.fatec.controle_financeiro.entities.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostMapping
    public ResponseEntity<Object> createCategoria(@RequestBody Categoria categoria) {
        // Validação: descrição não pode ser nula ou vazia
        if (categoria.getDescricao() == null || categoria.getDescricao().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: A descrição é obrigatória e não pode ser vazia.");
        }

        // Validação: descrição única
        if (categoriaRepository.existsByDescricao(categoria.getDescricao())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Erro: Já existe uma categoria com a descrição '" + categoria.getDescricao() + "'.");
        }

        // Define o valor padrão para o campo ativo, caso não seja enviado
        if (categoria.getAtivo() == null) {
            categoria.setAtivo(true);
        }

        // Salva a categoria no banco
        Categoria novaCategoria = categoriaRepository.save(categoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }
}
