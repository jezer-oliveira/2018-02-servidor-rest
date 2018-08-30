/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ds.jezer.servidorRest.controle;

import br.edu.ifrs.restinga.ds.jezer.servidorRest.dao.ProdutoDAO;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.erros.NaoEncontrado;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.modelo.Produto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class Produtos {
    
    //ArrayList<Produto> produtos;
    //int cont=1;

    @Autowired
    ProdutoDAO produtoDAO;
    
    @RequestMapping(path= "/produtos/", method =RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Produto> listar() {
        return produtoDAO.findAll();
    }

    @RequestMapping(path ="/produtos/{id}", method =RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Produto recuperar(@PathVariable int id) {
        Optional<Produto> optProduto = produtoDAO.findById(id);
        if(optProduto.isPresent()) {
            return optProduto.get();
        }
        else { 
            throw new NaoEncontrado("ID não encontrada");
        }
        //return null;
    }

    
    
/*
Versão com  ResponseEntity
    @RequestMapping(path ="/produtos/{id}", method =RequestMethod.GET)
    public ResponseEntity<Produto> recuperar(@PathVariable int id) {
        Optional<Produto> optProduto = produtoDAO.findById(id);
        if(optProduto.isPresent()) {
            return ResponseEntity.ok(optProduto.get());
        }
        else { 
            return ResponseEntity.notFound().build();
        }
        //return null;
    }
*/
    @RequestMapping(path = "/produtos/", method =  RequestMethod.POST)
    public Produto inserir(@RequestBody Produto produto) {
        produto.setId(0);
        produtoDAO.save(produto);
        return produto;
    }
    @RequestMapping(path = "/produtos/{id}", method = RequestMethod.DELETE)
    public void apagar(@PathVariable int id) {
        produtoDAO.deleteById(id);
    }

            
    
    @RequestMapping(path = "/produtos/{id}", method = RequestMethod.PUT)
    public void atualizar(@PathVariable int id,@RequestBody Produto produtoNovo) {
       
        //produtoDAO.save(produtoNovo);
        
        Produto produtoAntigo = this.recuperar(id);
        produtoAntigo.setNome(produtoNovo.getNome());
        produtoAntigo.setValor(produtoNovo.getValor());
        produtoAntigo.setDescricao(produtoNovo.getDescricao());
        
        produtoDAO.save(produtoAntigo);
                
    
    }
}


