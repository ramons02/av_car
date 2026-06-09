package br.edu.senai.fatesg.avcar.business.clientes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class ClienteDataSeeder implements CommandLineRunner {

    private final ClienteController clienteController;

    public ClienteDataSeeder(ClienteController clienteController) {
        this.clienteController = clienteController;
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> {
            try {
                // Verifica se já existem clientes suficientes para não ficar criando toda vez que o app reinicia
                if (clienteController.listar(false).getBody().size() >= 100) {
                    return; // Banco já está populado
                }

                System.out.println("Populando o banco de dados com 100 clientes fictícios (Em background, sem travar as telas!)...");

                String[] nomes = {"João", "Maria", "José", "Ana", "Carlos", "Paula", "Marcos", "Fernanda", "Rafael", "Juliana", "Lucas", "Mariana", "Pedro", "Camila", "Bruno", "Amanda"};
                String[] sobrenomes = {"Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes", "Costa", "Ribeiro", "Martins", "Carvalho", "Melo"};
                String[] cidades = {"São Paulo", "Rio de Janeiro", "Belo Horizonte", "Curitiba", "Porto Alegre", "Goiânia", "Brasília", "Salvador", "Fortaleza", "Recife"};
                String[] estados = {"SP", "RJ", "MG", "PR", "RS", "GO", "DF", "BA", "CE", "PE"};

                Random rand = new Random();

                for (int i = 0; i < 100; i++) {
            String nomeStr = nomes[rand.nextInt(nomes.length)] + " " + sobrenomes[rand.nextInt(sobrenomes.length)];
            String cidade = cidades[rand.nextInt(cidades.length)];
            String estado = estados[rand.nextInt(estados.length)];
            
            // CPF fictício de 11 dígitos
            String cpf = String.format("%03d%03d%03d%02d", rand.nextInt(1000), rand.nextInt(1000), rand.nextInt(1000), rand.nextInt(100));
            
            // Telefone fictício (Brasil): DDD + 9 + 8 dígitos = 11 dígitos
            String ddd = String.format("%02d", 11 + rand.nextInt(89));
            String tel = ddd + "9" + String.format("%04d", rand.nextInt(10000)) + String.format("%04d", rand.nextInt(10000));
            
            String email = nomeStr.toLowerCase().replace(" ", ".") + "@emailficticio.com";

            ClienteController.CriarPFRequest req = new ClienteController.CriarPFRequest(
                    nomeStr,
                    "Rua Fictícia, " + (rand.nextInt(900) + 100),
                    "Centro",
                    cidade,
                    estado,
                    "70000000",
                    tel,
                    email,
                    cpf,
                    "1234567",
                    LocalDate.of(1960 + rand.nextInt(40), 1 + rand.nextInt(12), 1 + rand.nextInt(28)),
                    "Cliente gerado automaticamente pelo Seeder Acadêmico"
            );

            clienteController.criarPF(req);
                }

                System.out.println("100 clientes fictícios foram inseridos com sucesso! Você pode dar refresh na tela agora.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}
