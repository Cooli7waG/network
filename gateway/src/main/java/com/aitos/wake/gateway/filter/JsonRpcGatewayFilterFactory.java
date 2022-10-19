package com.aitos.wake.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 验证码过滤器
 *
 * @author ruoyi
 */
//@Component
//@Slf4j
public class JsonRpcGatewayFilterFactory extends AbstractGatewayFilterFactory<Object>
{
    private final static String[] VALIDATE_URL = new String[] { "/system/auth/login", "/system/auth/register" };

    private static final String CODE = "code";

    private static final String UUID = "uuid";

    @Override
    public GatewayFilter apply(Object config)
    {
        return (exchange, chain) -> {
            URI uri = null;//exchange.getRequest().getURI();
            try {
                uri = new URI("lb://account");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            StringBuilder query = new StringBuilder();
            //获取请求uri的请求参数（GET请求参数通过拼接key=value形式进行传参）
            String originalQuery = uri.getRawQuery();

            //判断最后一个字符是否是&，如果不是则拼接一个&，以备后续的参数进行连接
            if (StringUtils.hasText(originalQuery)) {
                query.append(originalQuery);
                if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                    query.append('&');
                }
            }
            //获取config中的key、value，然后拼接到uri请求参数后面
            String value = ServerWebExchangeUtils.expand(exchange, config.toString());
            // TODO urlencode?
            query.append(config);
            query.append('=');
            query.append(value);
            //把请求参数重新拼接回去，并放入request中传递到过滤链的下一个请求中去
            try {
                URI newUri = UriComponentsBuilder.fromUri(uri)
                        .replaceQuery(query.toString()).build(true).toUri();

                ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri)
                        .build();

                return chain.filter(exchange.mutate().request(request).build());
            } catch (RuntimeException ex) {
                throw new IllegalStateException(
                        "Invalid URI query: \"" + query.toString() + "\"");
            }
        };
    }
}
