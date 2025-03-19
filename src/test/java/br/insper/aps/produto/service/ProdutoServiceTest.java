package br.insper.aps.produto.service;

import br.insper.aps.produto.Produto;
import br.insper.aps.produto.ProdutoRepository;

import br.insper.aps.produto.ProdutoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {
    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void test_saveProdutoSuccessfully(){
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto teste");
        produto.setPreco(100.0);
        produto.setEstoque(10);

        Mockito.when(produtoRepository.save(produto)).thenReturn(produto);

        Produto produtoCriado = produtoService.criarProduto(produto);
        Assertions.assertNotNull(produtoCriado);
        Assertions.assertEquals("Produto teste", produtoCriado.getNome());
    }

    @Test
    void test_getProdutoPorIdSuccessfully(){
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto teste");
        produto.setPreco(100.0);
        produto.setEstoque(10);

        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        Produto produtoRetornado = produtoService.getProdutoPorId("1");

        Assertions.assertNotNull(produtoRetornado);
        Assertions.assertEquals("Produto teste", produtoRetornado.getNome());
    }

    @Test
    void test_getProdutoPorIdNotFound(){
        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.empty());

        Produto produtoRetornado = produtoService.getProdutoPorId("1");
        Assertions.assertNull(produtoRetornado);
    }

    @Test
    void test_diminuirEstoqueSuccessfully(){
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto teste");
        produto.setPreco(100.0);
        produto.setEstoque(10);

        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);

        produtoService.diminuirEstoque("1", 5);
        Assertions.assertEquals(5, produto.getEstoque());

    }

    @Test
    void test_diminuirEstoqueInsuficiente(){
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto teste");
        produto.setPreco(100.0);
        produto.setEstoque(3);

        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            produtoService.diminuirEstoque("1", 5);
        });

        Assertions.assertEquals("Estoque insuficiente.", thrown.getMessage());
    }

    @Test
    void test_diminuirEstoqueProdutoNaoEncontrado(){
        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.empty());

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            produtoService.diminuirEstoque("1", 5);
        });

        Assertions.assertEquals("Produto não encontrado.", thrown.getMessage());
    }

    @Test
    void test_listarProdutos(){
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto teste");
        produto.setPreco(100.0);
        produto.setEstoque(10);

        List<Produto> produtos = new ArrayList<>();
        produtos.add(produto);

        Mockito.when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> resultado = produtoService.listarProdutos();
        Assertions.assertFalse(resultado.isEmpty());
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Produto teste", resultado.get(0).getNome());
    }

}
