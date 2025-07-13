package com.resume.resume_analyzer.service.impl;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiResumeLinkedinAnalyzerService {

    @Value("${openai.api.key}")
    private String apiKey;

    public String analyzeText(String promptText) {
        OpenAiService service = new OpenAiService(apiKey);

        ChatMessage systemMessage = new ChatMessage("system", "You are a professional resume and LinkedIn reviewer.");
        ChatMessage userMessage = new ChatMessage("user", promptText);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(systemMessage, userMessage))
                .maxTokens(800)
                .build();

        ChatCompletionResult result = service.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }
}
