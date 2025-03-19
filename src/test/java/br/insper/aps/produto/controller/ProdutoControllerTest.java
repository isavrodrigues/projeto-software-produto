package br.insper.aps.produto.controller;
import br.insper.aps.produto.Produto;
import br.insper.aps.produto.ProdutoController;
import br.insper.aps.produto.ProdutoRepository;

import br.insper.aps.produto.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {
    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(produtoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void test_listarProdutos() throws Exception {
        Produto produto1 = new Produto();
        produto1.setId("1");
        produto1.setNome("Produto A");
        produto1.setPreco(50.0);
        produto1.setEstoque(10);

        Produto produto2 = new Produto();
        produto2.setId("2");
        produto2.setNome("Produto B");
        produto2.setPreco(30.0);
        produto2.setEstoque(5);

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        Mockito.when(produtoService.listarProdutos()).thenReturn(produtos);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produto"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produtos)));
    }

    @Test
    void test_criarProduto() throws Exception {
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto Teste");
        produto.setPreco(100.0);
        produto.setEstoque(10);

        Mockito.when(produtoService.criarProduto(Mockito.any(Produto.class))).thenReturn(produto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/produto")
                        .content(objectMapper.writeValueAsString(produto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produto)));
    }

    @Test
    void test_getProdutoPorId() throws Exception {
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto Teste");
        produto.setPreco(100.0);
        produto.setEstoque(10);

        Mockito.when(produtoService.getProdutoPorId("1")).thenReturn(produto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produto/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produto)));
    }

    @Test
    void test_diminuirEstoque() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/produto/diminuir-estoque/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\": 5}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
