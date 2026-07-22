package com.oyku.blog.Security.ratelimit;

import java.io.IOException;
import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

	private final StringRedisTemplate redisTemplate;

	private static final int MAX_REQUESTS = 10;
	private static final int WINDOW_SECONDS = 60;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String ip = request.getRemoteAddr();
		String key = "rate_limit:" + ip;

		Long requestCount = redisTemplate.opsForValue().increment(key);
		
	if (requestCount == 1) {

			redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
		}

		if (requestCount > MAX_REQUESTS) {
		    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		    response.setContentType("application/json");
		    response.getWriter().write("""
		        {
		          "status":429,
		          "error":"Too Many Requests",
		          "message":"Too many requests. Please try again later."
		        }
		        """);
		    return;
		}
		
	
		filterChain.doFilter(request, response);
	}

}
