package br.edu.senai.fatesg.avcar.swing.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> List<T> getList(String path, TypeReference<List<T>> typeRef) {
        return getList(path, typeRef, null);
    }

    public <T> List<T> getList(String path, Class<T> elementClass) {
        return getList(path, null, elementClass);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getList(String path, TypeReference<List<T>> typeRef, Class<T> elementClass) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                if (elementClass != null) {
                    var javaType = mapper.getTypeFactory().constructCollectionType(List.class, elementClass);
                    return mapper.readValue(resp.body(), javaType);
                }
                return mapper.readValue(resp.body(), typeRef);
            }
            throw new RuntimeException("Erro " + resp.statusCode() + ": " + resp.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição: " + e.getMessage(), e);
        }
    }

    public <T> T getOne(String path, Class<T> type) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                return mapper.readValue(resp.body(), type);
            }
            throw new RuntimeException("Erro " + resp.statusCode() + ": " + resp.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição: " + e.getMessage(), e);
        }
    }

    public void post(String path, Object body) {
        try {
            String json = mapper.writeValueAsString(body);
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(30))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                throw new RuntimeException("Erro " + resp.statusCode() + ": " + resp.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição: " + e.getMessage(), e);
        }
    }

    public <T> T postWithResponse(String path, Object body, Class<T> responseType) {
        try {
            String json = mapper.writeValueAsString(body);
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(30))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                return mapper.readValue(resp.body(), responseType);
            }
            throw new RuntimeException("Erro " + resp.statusCode() + ": " + resp.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição: " + e.getMessage(), e);
        }
    }

    public void patch(String path) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(30))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                throw new RuntimeException("Erro " + resp.statusCode() + ": " + resp.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição: " + e.getMessage(), e);
        }
    }

    public void delete(String path) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .timeout(Duration.ofSeconds(30))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                throw new RuntimeException("Erro " + resp.statusCode() + ": " + resp.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição: " + e.getMessage(), e);
        }
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        try {
            String json = mapper.writeValueAsString(body);
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(30))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                return mapper.readValue(resp.body(), responseType);
            }
            throw new RuntimeException("Erro " + resp.statusCode() + ": " + resp.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição: " + e.getMessage(), e);
        }
    }
}
