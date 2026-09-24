package com.parth.saloonmanagement.service;

import com.parth.saloonmanagement.dto.AuthResponse;
import com.parth.saloonmanagement.dto.CustomerSignupRequest;
import com.parth.saloonmanagement.dto.LoginRequest;
import com.parth.saloonmanagement.dto.SignUpRequest;
import com.parth.saloonmanagement.entity.Role;
import com.parth.saloonmanagement.entity.Tenant;
import com.parth.saloonmanagement.entity.User;
import com.parth.saloonmanagement.exception.InvalidCredentialsException;
import com.parth.saloonmanagement.exception.ResourceNotFoundException;
import com.parth.saloonmanagement.exception.UserAlreadyExistsException;
import com.parth.saloonmanagement.repository.TenantRepository;
import com.parth.saloonmanagement.repository.UserRepository;
import com.parth.saloonmanagement.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {



    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }




    //Login
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found "));

        if(!passwordEncoder.matches(request.getPassword() , user.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }


        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .email(user.getEmail())
                .role(user.getRole())
                .tenantId(user.getTenant().getId())
                .build();

    }

    //SingUp
    public AuthResponse signUp(SignUpRequest request){
        String username = request.getEmail();

        if(userRepository.findByEmail(username).isPresent()){
            throw new UserAlreadyExistsException("User already exists");
        }

        Tenant tenant = new Tenant();
        tenant.setName(request.getTenantName());
        tenant.setAddress(request.getTenantAddress());
        tenant.setContact(request.getContact());
        tenant.setEmail(request.getEmail());

        Tenant savedTenant = tenantRepository.save(tenant);


        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setContact(request.getContact());

        user.setRole(Role.ROLE_OWNER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setTenant(savedTenant);
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .tenantId(savedUser.getTenant().getId())
                .build();
    }

    public AuthResponse customerSignUp(CustomerSignupRequest request){
        String email = request.getEmail();

        if(userRepository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistsException("User already exists");
        }

        Tenant tenant = tenantRepository.findById(request.getTenantId()).
                orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));


        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setContact(request.getContact());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_CUSTOMER);

        user.setTenant(tenant);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .tenantId(savedUser.getTenant().getId())

                .build();
    }



}
