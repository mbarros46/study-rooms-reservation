package br.com.fiap.epictaskg.config;

import br.com.fiap.epictaskg.user.Role;
import br.com.fiap.epictaskg.user.User;
import br.com.fiap.epictaskg.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attrs = oauth2User.getAttributes();

        String providerUserNameAttr = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        String email;
        String name;

        Object emailObj = attrs.get("email");
        String emailStr = emailObj != null ? String.valueOf(emailObj) : null;

        Object nameObj = attrs.get("name");
        String nameStr = nameObj != null ? String.valueOf(nameObj) : null;

        if (emailStr == null || emailStr.isBlank()) {
            // GitHub pode não retornar email público; cai para ID/username
            Object loginObj = attrs.get("login");
            if (loginObj != null) {
                emailStr = loginObj + "@users.noreply.github.com";
                if (nameStr == null || nameStr.isBlank()) nameStr = String.valueOf(loginObj);
            }
        }

        email = (emailStr != null && !emailStr.isBlank()) ? emailStr : "unknown@example.com";
        name = (nameStr != null && !nameStr.isBlank()) ? nameStr : "Unknown";

        Optional<User> existing = userRepository.findByEmail(email);
        User user = existing.orElseGet(() -> userRepository.save(new User(name, email, Role.STUDENT)));

        return new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                attrs,
                providerUserNameAttr != null && !providerUserNameAttr.isBlank() ? providerUserNameAttr : "sub"
        );
    }
}
