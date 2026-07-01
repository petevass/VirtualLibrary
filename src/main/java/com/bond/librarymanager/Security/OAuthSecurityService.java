package com.bond.librarymanager.Security;

import com.bond.librarymanager.Model.ApplicationUser;
import com.bond.librarymanager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class OAuthSecurityService extends DefaultOAuth2UserService {

    @Autowired
    UserRepository userRepository;

    Set<String> librarians = new HashSet<>();
    Set<String> administrators =  new HashSet<>();
    Set<String> owners =   new HashSet<>();

    public OAuthSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;


    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)  {
        boolean exists = true;
        for(ApplicationUser librarian: userRepository.findAllByRole("LIBRARIAN")){
            librarians.add(librarian.getEmail());
        }

        for(ApplicationUser admin: userRepository.findAllByRole("ADMIN")){
            administrators.add(admin.getEmail());
        }

        for(ApplicationUser owner: userRepository.findAllByRole("OWNER")){
            owners.add(owner.getEmail());
        }
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ApplicationUser applicationUser = new ApplicationUser();

        if(!userRepository.existsByEmail(oAuth2User.getAttribute("email"))){
            applicationUser.setEmail(oAuth2User.getAttribute("email"));
            applicationUser.setName(oAuth2User.getAttribute("name"));
            applicationUser.setRole("USER");
            exists = false;
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        String email =  oAuth2User.getAttribute("email");
        if(email != null && librarians.contains(email)){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_LIBRARIAN"));
            if(!exists){
                applicationUser.setRole("LIBRARIAN");
            }
        }

        if(administrators.contains(email) && email!=null){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

            if(!exists){
                applicationUser.setRole("ADMIN");
            }
        }

        if(owners.contains(email) && email!=null){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_LIBRARIAN"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            if(!exists){
                applicationUser.setRole("OWNER");
            }
        }
        String nameAttributeKey = "email";

        if(!exists){
            userRepository.save(applicationUser);
        }

        return new DefaultOAuth2User(grantedAuthorities, oAuth2User.getAttributes(), nameAttributeKey);
    }



}
