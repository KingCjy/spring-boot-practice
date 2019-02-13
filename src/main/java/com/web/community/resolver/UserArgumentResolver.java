package com.web.community.resolver;

import com.web.community.annotation.SocialUser;
import com.web.community.domain.User;
import com.web.community.domain.enums.SocialType;
import com.web.community.repository.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private UserRepository userRepository;

    public UserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(SocialUser.class) != null && methodParameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpSession httpSession = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();

        User user = (User) httpSession.getAttribute("user");

        return getUser(user, httpSession);
    }

    private User getUser(User user, HttpSession httpSession) {
        if(user == null) {
            try {
                OAuth2Authentication auth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

                Map<String, String> map = (HashMap<String, String>) auth2Authentication.getUserAuthentication().getDetails();

                User convertUser = convertUser(String.valueOf(auth2Authentication.getAuthorities().toArray()[0]), map);
                user = userRepository.findByEmail(convertUser.getEmail());

                if(user == null) {
                    user = userRepository.save(convertUser);
                }

                setRoleIfNotSame(user, auth2Authentication, map);
                httpSession.setAttribute("user", user);
            } catch(Exception e) {
                return user;
            }
        }
        return user;
    }


    private User convertUser(String authority, Map<String, String> map) {
        if(SocialType.KAKAO.isEquals(authority)) return getKakaoUser(map);

        return null;
    }

    private User getKakaoUser(Map<String, String> map) {
        HashMap<String, String> propertyMap = (HashMap<String, String>)(Object) map.get("properties");

        return User.builder()
                .name(propertyMap.get("nickname"))
                .email(map.get("kaccount_email"))
                .principal(String.valueOf(map.get("id")))
                .socialType(SocialType.KAKAO)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private void setRoleIfNotSame(User user, OAuth2Authentication oAuth2Authentication, Map<String, String> map) {
        if(!oAuth2Authentication.getAuthorities().contains(new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType())));
        }
    }
}
