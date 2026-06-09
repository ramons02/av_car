package br.edu.senai.fatesg.avcar.core.controllers.response;

import java.util.List;

public class ApiResponse<T> {

    private boolean sucesso;
    private String mensagem;
    private T dados;
    private List<String> erros;

    public ApiResponse() {}

    private ApiResponse(boolean sucesso, String mensagem, T dados, List<String> erros) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
        this.erros = erros;
    }

    public static <T> ApiResponse<T> ok(T dados) {
        return new ApiResponse<>(true, "Operação realizada com sucesso", dados, null);
    }

    public static <T> ApiResponse<T> criado(T dados) {
        return new ApiResponse<>(true, "Recurso criado com sucesso", dados, null);
    }

    public static <T> ApiResponse<T> erro(String mensagem) {
        return new ApiResponse<>(false, mensagem, null, List.of(mensagem));
    }

    public static <T> ApiResponse<T> erro(String mensagem, List<String> erros) {
        return new ApiResponse<>(false, mensagem, null, erros);
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public T getDados() { return dados; }
    public List<String> getErros() { return erros; }
}
