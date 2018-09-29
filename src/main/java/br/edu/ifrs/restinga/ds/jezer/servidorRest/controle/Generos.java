/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ds.jezer.servidorRest.controle;

import br.edu.ifrs.restinga.ds.jezer.servidorRest.dao.GeneroDAO;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.dao.ProdutoDAO;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.erros.NaoEncontrado;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.modelo.Genero;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.modelo.Produto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jezer
 */
@RestController
@RequestMapping(path = "/api")
public class Generos {
    
    @Autowired
    GeneroDAO generoDAO; 
    @Autowired 
    ProdutoDAO produtoDAO;
    
    @RequestMapping(path = "/generos/", method = RequestMethod.GET)
    public Iterable<Genero> listar() {
        return generoDAO.findAll();
    }

    
    @RequestMapping(path = "/generos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Genero inserir(@RequestBody Genero genero) {
        genero.setId(0);
        Genero generoSalvo = generoDAO.save(genero);
        return generoSalvo;
    }

    @RequestMapping(path = "/generos/{id}", method = RequestMethod.GET)
    public Genero recuperar(@PathVariable int id) {
        Optional<Genero> findById = generoDAO.findById(id);
        if(findById.isPresent())
            return findById.get();
         else 
            throw new NaoEncontrado("Não encontrado");
    }
    
    @RequestMapping(path = "/generos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Genero genero){
        if(generoDAO.existsById(id)){
            genero.setId(id);
            generoDAO.save(genero);
        } else 
            throw new NaoEncontrado("Não encontrado");
    
    }
    
    @RequestMapping(path= "/generos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if(generoDAO.existsById(id)){
            generoDAO.deleteById(id);
        } else 
            throw new NaoEncontrado("Não encontrado");
        
    }

    @RequestMapping(path = "/generos/{id}/produtos/", method = RequestMethod.GET)
    public Iterable<Produto> listarProdutos(@PathVariable int id) {
        Optional<Genero> findById = generoDAO.findById(id);
        if(findById.isPresent())
            return  produtoDAO.findByGenero(findById.get());
         else 
            throw new NaoEncontrado("Não encontrado");
    }

}
