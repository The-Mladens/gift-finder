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
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static com.generated_jooq.tables.MdQuestionTopicTemplate.MD_QUESTION_TOPIC_TEMPLATE;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.impl.DSL;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import com.db.*;
import com.generated_jooq.tables.pojos.MdQuestionTopicTemplate;
import com.db.SecretKeyValidator;

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

                // Създаване на връзка с базата данни
                Connection conn;
                conn = PgDataSource.getConnectionSafely();

                // Validate the secret key using the new SecretKeyValidator class
                boolean isValidKey;
                try {
                    isValidKey = SecretKeyValidator.validateSecretKey(secretKey, conn);
                } catch (SQLException e) {
                    context.getLogger().severe("Database error: " + e.getMessage());
                    return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Database error")
                                    .build();
                }

                if (!isValidKey) {
                    return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                                    .body("Невалиден ключ")
                                    .build();
                }

                // Create a DSLContext for interacting with the database using the connection
                DSLContext dslContext = null;
                dslContext = DSL.using(conn);

                // Извличане на темите за анализ
                Result<Record> result = dslContext.select().from(MD_QUESTION_TOPIC_TEMPLATE).fetch();                

                String  systemMessageTxt = null;
                for (Record r : result) {
                        systemMessageTxt = systemMessageTxt + r.getValue(MD_QUESTION_TOPIC_TEMPLATE.SYSTEM_PROMPT);
                    }
                SystemMessage  systemMessage = SystemMessage.of(systemMessageTxt);

                String apiKey = System.getenv("OPENAI_API_KEY");

                // Извикване на OpenAI API
                var openAI = SimpleOpenAI.builder()
                                // .apiKey(System.getenv("OPENAI_API_KEY"))
                                .apiKey(apiKey)
                                .build();

                // Изпращане на заявка към OpenAI
                var chatRequest = ChatRequest.builder()
                                .model("gpt-4o-mini")
                                .message(systemMessage)
                                .message(UserMessage.of(text))
                                .temperature(0.5)
                                .maxCompletionTokens(100)
                                .build();
                var futureChat = openAI.chatCompletions().create(chatRequest);
                var chatResponse = futureChat.join();

                // Construct the JSON response using ObjectMapper.
                ObjectMapper responseMapper = new ObjectMapper();
                ObjectNode jsonResponseNode = responseMapper.createObjectNode();
                jsonResponseNode.put("role", "ai");
                jsonResponseNode.put("text", chatResponse.firstContent());
                String jsonResponse;
                try {
                        jsonResponse = responseMapper.writeValueAsString(jsonResponseNode);
                } catch (JsonProcessingException e) {
                        context.getLogger().severe("Failed to construct JSON response: " + e.getMessage());
                        return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Error constructing JSON response")
                                        .build();
                }

                return request.createResponseBuilder(HttpStatus.OK)
                                .header("Content-Type", "application/json")
                                .body(jsonResponse)
                                .build();
        }
}