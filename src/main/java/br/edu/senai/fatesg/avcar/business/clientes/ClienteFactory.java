package br.edu.senai.fatesg.avcar.business.clientes;

// PADRÃO FACTORY METHOD: Define uma interface para criar objetos, mas permite
// que subclasses decidam qual classe concreta instanciar. Aplicado aqui para
// centralizar a criação de PessoaFisica ou PessoaJuridica conforme o tipo
// recebido, isolando o código cliente da lógica de escolha e dos construtores.
public class ClienteFactory {

    private ClienteFactory() {}

    public static Cliente criarPessoaFisica(Long id, String nome, String endereco,
                                            String telefone, String email, String cpf) {
        return new PessoaFisica(id, nome, endereco, telefone, email, cpf);
    }

    public static Cliente criarPessoaJuridica(Long id, String nome, String endereco,
                                              String telefone, String email,
                                              String cnpj, String inscricaoEstadual) {
        return new PessoaJuridica(id, nome, endereco, telefone, email, cnpj, inscricaoEstadual);
    }

    public static Cliente criar(String tipo, Long id, String nome, String endereco,
                                String telefone, String email, String documento) {
        return switch (tipo.toUpperCase()) {
            case "PF" -> new PessoaFisica(id, nome, endereco, telefone, email, documento);
            case "PJ" -> new PessoaJuridica(id, nome, endereco, telefone, email, documento, null);
            default -> throw new IllegalArgumentException("Tipo inválido: " + tipo);
        };
    }
}
