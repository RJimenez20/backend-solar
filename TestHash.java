import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String plain = "123456";
        String hash = encoder.encode(plain);
        System.out.println("HASH_GENERADO_PARA_123456: " + hash);
    }
}
