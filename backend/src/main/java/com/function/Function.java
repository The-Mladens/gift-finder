package com.function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.Optional;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

public class Function {

 
    /**
     * This function listens at endpoint "/api/GiftMessageFunction" and accepts POST
     * requests.
     * It ignores the request body and always returns a JSON response with a message
     * "ОК".
     */
    @FunctionName("MessageFunctionTest")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Processing GiftMessageFunction request.");

        // Retrieve the request body (ignored for this implementation)
        String requestBody = request.getBody().orElse("");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        String text;
        String secretKey;
        try {
            rootNode = objectMapper.readTree(requestBody);

            // Извличане на текста от първия елемент в "messages"
            text = rootNode.get("messages").get(0).get("text").asText();
            secretKey = rootNode.get("sk").asText();
        } catch (JsonProcessingException e) {
            context.getLogger().severe("Failed to parse JSON: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Invalid JSON format")
                    .build();
        }

        String mySecretKey = System.getenv("MY_SECRET_KEY");
        mySecretKey = Objects.requireNonNullElse(mySecretKey, "test-key");

        // Сравняваме secretKey правилно с .equals()
        if (!mySecretKey.equals(secretKey)) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Error 101")
                    .build();
        }

        String apiKey = System.getenv("OPENAI_API_KEY");
        
        // Извикване на OpenAI API
        var openAI = SimpleOpenAI.builder()
                //.apiKey(System.getenv("OPENAI_API_KEY"))
                .apiKey(apiKey)
                .build();
        // Изпращане на заявка към OpenAI
        var chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(SystemMessage.of("You are an expert in AI."))
                .message(UserMessage.of(text))
                .temperature(0.5)
                .maxCompletionTokens(100)
                .build();
        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();

/* 
        chatElementRef.requestInterceptor = (requestDetails) => {
                console.log(requestDetails); // printed above
                requestDetails.body = {prompt: requestDetails.body.messages[0].text}; // custom body
                return requestDetails;
              };
 */              
        // Manually construct the JSON response string without using ObjectMapper.
        String jsonResponse = "{\"role\": \"ai\", \"text\": \"" + chatResponse.firstContent() + "\"}";

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonResponse)
                .build();
    }
}