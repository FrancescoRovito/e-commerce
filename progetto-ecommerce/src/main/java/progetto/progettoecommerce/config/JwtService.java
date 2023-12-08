package progetto.progettoecommerce.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import progetto.progettoecommerce.entities.User;

@Service
public class JwtService {

    private static final String SECRET_KEY="357538782F4125442A472D4B6150645367566B59703373367639792442264528";

    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsernameFromRequest(HttpServletRequest request){
        final String authHeader=request.getHeader("Authorization");
        final String jwtToken=authHeader.substring(7);  
        return extractUsernameFromToken(jwtToken);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> claims, User user){
        return Jwts.builder()
               .setClaims(claims)
               .setSubject(user.getEmail())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis()+1000*60*24)) 
               .signWith(getSignInKey(), SignatureAlgorithm.HS256)
               .compact(); 
    }

    public boolean isTokenValid(String token, User user){
        final String username=extractUsernameFromToken(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token); 
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims (String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }
    
    private Key getSignInKey() { 
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
