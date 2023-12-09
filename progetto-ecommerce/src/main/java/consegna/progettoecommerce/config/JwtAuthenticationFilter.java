package consegna.progettoecommerce.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.repositories.UsersRepository;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        
            final String authorizationHeader=request.getHeader("Authorization");
            final String jwtToken;
            final String username;
            if(authorizationHeader==null || !authorizationHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;   
            }
            jwtToken=authorizationHeader.substring(7);
            username=jwtService.extractUsernameFromToken(jwtToken);
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                User user= usersRepository.findByEmail(username);
                if(jwtService.isTokenValid(jwtToken, user)){     
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,   
                    user.getAuthorities()
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
    }
}
    

