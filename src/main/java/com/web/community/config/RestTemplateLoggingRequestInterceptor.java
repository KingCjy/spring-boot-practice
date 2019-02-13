package com.web.community.config;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class RestTemplateLoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * <pre>
     * intercept
     *
     * <pre>
     *
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // 사용자 정보 추출
        String userId = "aaaa";

        // request log
        URI uri = request.getURI();
        traceRequest(request, body, userId);

        // execute
        ClientHttpResponse response = execution.execute(request, body);

        // response log
        traceResponse(response, userId, uri);
        return response;
    }

    /**
     * <pre>
     * traceRequest
     *
     * <pre>
     * @param request
     * @param body
     * @param userId
     * @throws IOException
     */
    private void traceRequest(HttpRequest request, byte[] body, String userId) throws IOException {
        StringBuilder reqLog = new StringBuilder();
        reqLog.append("[REQUEST] ");
        reqLog.append("Uri : " + request.getURI());
        reqLog.append(", Method : " + request.getMethod());
        reqLog.append(", Request Body : " + new String(body, StandardCharsets.UTF_8));
        reqLog.append(", UserId : " + userId);
        log.info(reqLog.toString());
    }

    /**
     * <pre>
     * traceResponse
     *
     * <pre>
     * @param response
     * @param userId
     * @throws IOException
     */
    private void traceResponse(ClientHttpResponse response, String userId, URI uri) throws IOException {
        InputStream is = response.getBody();
        byte[] bodyData = IOUtils.toByteArray(is);

        StringBuilder resLog = new StringBuilder();
        resLog.append("[RESPONSE] ");
        resLog.append("Uri : " + uri);
        resLog.append(", Status code : " + response.getStatusCode());
        resLog.append(", Response Body : " + new String(bodyData, StandardCharsets.UTF_8));
        resLog.append(", UserId : " + userId);
        log.info(resLog.toString());
    }
}