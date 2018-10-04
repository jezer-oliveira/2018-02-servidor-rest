/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.ds.jezer.servidorRest.controle;

import br.edu.ifrs.restinga.ds.jezer.servidorRest.dao.FornecedorDAO;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.dao.ModeloDAO;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.dao.ProdutoDAO;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.erros.NaoEncontrado;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.erros.RequisicaoInvalida;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.modelo.Fornecedor;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.modelo.Modelo;
import br.edu.ifrs.restinga.ds.jezer.servidorRest.modelo.Produto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jezer
 */
@RestController
@RequestMapping(path = "/api")
public class Produtos {
    @Autowired
    ProdutoDAO produtoDAO;
    @Autowired
    ModeloDAO modeloDAO;
    
    @Autowired
    FornecedorDAO fornecedorDAO;
   
    @RequestMapping( path = "/produtos/pesquisar/nome/", method = RequestMethod.GET)
    public Iterable<Produto> pesquisaPorNome(
            @RequestParam(required = false) String inicia, 
            @RequestParam(required = false) String contem ) {
        if(inicia!=null) {
            return produtoDAO.findByNomeStartingWith(inicia);
        }
        if(contem!=null) {
            return produtoDAO.findByNomeContaining(contem);
        }
        
        throw new RequisicaoInvalida("Indique um dos 2 valores");
    
    }
    
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
        produtoAntigo.setEmbalagem(produtoNovo.getEmbalagem());
        produtoAntigo.setGenero(produtoNovo.getGenero());
        
        produtoDAO.save(produtoAntigo);
    }
    
    
    @RequestMapping(path = "/produtos/{idProduto}/modelos/", 
            method = RequestMethod.GET)
    public Iterable<Modelo> listarModelo(@PathVariable int idProduto) {
        return this.recuperar(idProduto).getModelos();
        /*
        Optional<Produto> optProduto = produtoDAO.findById(idProduto);
        if(optProduto.isPresent()) {
            Produto produto= optProduto.get();
            return produto.getModelos();
        }
        else { 
            throw new NaoEncontrado("ID não encontrada");
        }

        
        
        */
       // 
    }

    
    @RequestMapping(path = "/produtos/{idProduto}/modelos/", 
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Modelo inserirModelo(@PathVariable int idProduto, 
            @RequestBody Modelo modelo) {
        modelo.setId(0);
        Modelo modeloSalvo = modeloDAO.save(modelo);
        Produto produto = this.recuperar(idProduto);
        produto.getModelos().add(modeloSalvo);
        produtoDAO.save(produto);
        return modeloSalvo;
    }

    @RequestMapping(path = "/produtos/{idProduto}/modelos/{id}", method = RequestMethod.GET)
    public Modelo recuperarModelo(@PathVariable int idProduto, @PathVariable int id) {
        Optional<Modelo> findById = modeloDAO.findById(id);
        if(findById.isPresent())
            return findById.get();
        else 
            throw new NaoEncontrado("Não encontrado");
    }
    
    @RequestMapping(path = "/produtos/{idProduto}/modelos/{id}", 
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizarModelo(@PathVariable int idProduto, @PathVariable int id, @RequestBody Modelo modelo){
        if(modeloDAO.existsById(id)){
            modelo.setId(id);
            modeloDAO.save(modelo);
        } else 
            throw new NaoEncontrado("Não encontrado");
    
    }
    
    @RequestMapping(path= "/produtos/{idProduto}/modelos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagarModelo(@PathVariable int idProduto, 
            @PathVariable int id) {
        
        Modelo modeloAchada=null;
        Produto produto = this.recuperar(idProduto);
        List<Modelo> modelos = produto.getModelos();
        for (Modelo modeloLista : modelos) {
            if(id==modeloLista.getId())
                modeloAchada=modeloLista;
        }
        if(modeloAchada!=null) {
            produto.getModelos().remove(modeloAchada);
            produtoDAO.save(produto);
        } else 
            throw new NaoEncontrado("Não encontrado");
    }




    
  @RequestMapping(path = "/produtos/{idProduto}/fornecedores/", 
            method = RequestMethod.GET)
    public Iterable<Fornecedor> listarFornecedor(@PathVariable int idProduto) {
        return this.recuperar(idProduto).getFornecedores();
    }

    
    @RequestMapping(path = "/produtos/{idProduto}/fornecedores/", 
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inserirFornecedor(@PathVariable int idProduto, 
            @RequestBody Fornecedor fornecedor) {
        Produto produto = this.recuperar(idProduto);
        produto.getFornecedores().add(fornecedor);
        produtoDAO.save(produto);
    }

    @RequestMapping(path = "/produtos/{idProduto}/fornecedores/{id}", method = RequestMethod.GET)
    public Fornecedor recuperarFornecedor(@PathVariable int idProduto, @PathVariable int id) {
        Produto produto = this.recuperar(idProduto);
        List<Fornecedor> fornecedores = produto.getFornecedores();
        for (Fornecedor fornecedor : fornecedores) {
            if(fornecedor.getId()==id)
                return fornecedor;
        }
        throw new NaoEncontrado("Não encontrado");
    }
    
       
    @RequestMapping(path= "/produtos/{idProduto}/fornecedores/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagarFornecedor(@PathVariable int idProduto, 
            @PathVariable int id) {
        
        Fornecedor fornecedorAchado=null;
        Produto produto = this.recuperar(idProduto);
        List<Fornecedor> fornecedores = produto.getFornecedores();
        for (Fornecedor fornecedorLista : fornecedores) {
            if(id==fornecedorLista.getId())
                fornecedorAchado=fornecedorLista;
        }
        if(fornecedorAchado!=null) {
            produto.getFornecedores().remove(fornecedorAchado);
            produtoDAO.save(produto);
        } else 
            throw new NaoEncontrado("Não encontrado");
    }

    
}


