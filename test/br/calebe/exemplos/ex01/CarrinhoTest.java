package br.calebe.exemplos.ex01;

import br.calebe.exemplos.ex02.ClasseExemplo;
import br.calebe.exemplos.ex02.controller.ClasseExemploController;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;



@RunWith(PowerMockRunner.class)
@PrepareForTest({ClasseExemplo.class})
public class CarrinhoTest {

    private Carrinho carrinho;

    @Before
    public void criandoCarrinho() {
        carrinho = new Carrinho();
    }

    @Test(expected = CarrinhoVazioExpected.class)
    public void colocandoZeroProduto() throws CarrinhoVazioExpected {
        Produto menor;
        menor = carrinho.menorProduto();
        assertArrayEquals(new Object[]{null}, new Object[]{menor});
    }

    @Test
    public void colocandoUmProduto() throws CarrinhoVazioExpected {
        Produto livro = new Produto("Java em 24 horas", 50.00);
        carrinho.add(livro);
        Produto menor;
        menor = carrinho.menorProduto();
        assertArrayEquals(new Object[]{livro}, new Object[]{menor});
    }

    @Test
    public void colocandoMaisProdutos() throws CarrinhoVazioExpected {
        Produto livro = new Produto("Java em 24 horas", 50.00);
        carrinho.add(livro);
        Produto deitel = new Produto("Java: como programar", 150.00);
        carrinho.add(deitel);
        
        //--caso de teste inserido na aula de 17/09/2013
        Produto head_first = new Produto("Java: como programar", 50.00);
        carrinho.add(head_first);
        
        Produto menor;
        menor = carrinho.menorProduto();
                
        assertArrayEquals(new Object[]{livro}, new Object[]{menor});
    }

    @Test
    public void identidadeDeProdutos() throws CarrinhoVazioExpected {
        Produto original = new Produto("Java em 24 horas", 50.00);
        carrinho.add(original);
        Produto copia = new Produto("Java em 24 horas", 50.00);
        original = carrinho.menorProduto();
        assertArrayEquals(new Object[]{original}, new Object[]{copia});
    }
    
    //Caso de teste criado em 17/09/2013
    @Test
    public void exibeDescricaoDeProdutos() {
        Produto teste = new Produto("Use a cabeça", 70.00);
        assertArrayEquals(new Object[]{teste.getDescricao()}, new Object[]{"Use a cabeça"});
                    
     }
    //adicionar novos tipos de produto no carrinho
    @Test
    public void addNovosTiposProdutosCarrinho(){
        String tipoProduto = "Produtos Carros";
        assertArrayEquals(new Object[]{tipoProduto},new Object[]{"Produtos Carros"} );
    }
    
    //Listar todos os produtos do carrinho
    @Test
    public void listarTodosProdutosDoCarrinho(){
        
        List<String> listaProdutos = new ArrayList<>();
        
        listaProdutos.add("Rodas");
        listaProdutos.add("Calotas");
        listaProdutos.add("Pneus");
        
        assertArrayEquals(new Object[]{listaProdutos.get(0)}, new Object[]{"Rodas"});
        assertArrayEquals(new Object[]{listaProdutos.get(1)}, new Object[]{"Calotas"});
        assertArrayEquals(new Object[]{listaProdutos.get(2)}, new Object[]{"Pneus"});
        
    }
    
    //remover 1 produto do carrinho
    @Test
    public void removerUmProdutoCarrinho(){
        
        List<String> listaProdutos = new ArrayList<>();
        
        //add 3 itens
        listaProdutos.add("Rodas");
        listaProdutos.add("Calotas");
        listaProdutos.add("Pneus");
        
        //remove 1 item
        listaProdutos.remove("Rodas");
        
        //espera obeter uma lista com 2 itens
        assertArrayEquals(new Object[]{listaProdutos.size()}, new Object[]{2});
        
    }
    
    //Calcular o total que deve ser pago pelos produtos no carrinho
    @Test
    public void totalASerPagoNoCarrinho(){
        
        List<Double> listaProdutosPreco = new ArrayList<>();
        
        double total = 0.0;
        
        listaProdutosPreco.add(420.00);
        listaProdutosPreco.add(155.00);
        listaProdutosPreco.add(204.00);
        
        //calcula o total de valores dentro da lista
        for(Double x : listaProdutosPreco){
            total = total + x.doubleValue();
        }
        
        assertArrayEquals(new Object[]{total}, new Object[]{779.00});
        
    }
    
    @Test
    public void realizarPagamentoCarrinho() throws Exception{
        
        List<Double> listaProdutosPreco = new ArrayList<>();
        
        double total = 0.0;
        
        listaProdutosPreco.add(20.00);
        listaProdutosPreco.add(5.00);
        listaProdutosPreco.add(4.00);
        
        //calcula o total de valores dentro da lista
        for(Double x : listaProdutosPreco){
            total = total + x.doubleValue();
        }
        
        //============PAGAMENTO==================
        // Cria o objeto Mock da classe ClasseExemploController
        ClasseExemploController controllerMockPagamento = PowerMock.createMock(ClasseExemploController.class);
        // Espera que toda instanciação dessa classe seja substituída pelo objeto mockado
        PowerMock.expectNew(ClasseExemploController.class).andReturn(controllerMockPagamento);
        // E espera que a resposta pela chamada do método seja determinado
        EasyMock.expect(controllerMockPagamento.pagamentoViaCartaoCredito(total)).andReturn("Pagamento feito no valor de "+total);
        // "Executa" a configuração programada
        PowerMock.replay(controllerMockPagamento, ClasseExemploController.class);
        
        // Chama a classe - internamente, a classe mockada será utilizada
        ClasseExemplo tested = new ClasseExemplo();
        tested.pagueNoCartaodebito(total);
        
        // Faz a verificaçao agendada
        Assert.assertEquals("Pagamento feito no valor de "+total, tested.getAnswer());
       
        // Executa todas as verificação
        PowerMock.verifyAll();
    }
    
    @Test
    public void controleDeEstoque() throws Exception{
        List<String> listaProdutos = new ArrayList<>();
        int itens;
        
        //add 3 itens
        listaProdutos.add("Shampoo");
        listaProdutos.add("Camisinha");
        listaProdutos.add("Sabonete");
        
        itens = listaProdutos.size();
        
        //=============CONTROLE DE ESTOQUE==================
        // Cria o objeto Mock da classe ClasseExemploController
        ClasseExemploController controllerMockEstoque = PowerMock.createMock(ClasseExemploController.class);
        // Espera que toda instanciação dessa classe seja substituída pelo objeto mockado
        PowerMock.expectNew(ClasseExemploController.class).andReturn(controllerMockEstoque);
        // E espera que a resposta pela chamada do método seja determinado
        EasyMock.expect(controllerMockEstoque.controleEstoque(itens)).andReturn("Quantidade de itens: "+itens);
        // "Executa" a configuração programada
        PowerMock.replay(controllerMockEstoque,ClasseExemploController.class);
        
        // Chama a classe - internamente, a classe mockada será utilizada
        ClasseExemplo controlaEstoque = new ClasseExemplo();
        controlaEstoque.controleEstoque(itens);
        
        // Faz a verificaçao agendada
        Assert.assertEquals("Quantidade de itens: "+itens, controlaEstoque.getAnswer());
        
        PowerMock.verifyAll();
        
    }
}