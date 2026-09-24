    package com.parth.saloonmanagement.security;


    import com.parth.saloonmanagement.repository.UserRepository;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.stereotype.Service;


    @Service
    public class UserDetailService implements UserDetailsService {

        private final UserRepository userRepository;

        public UserDetailService(UserRepository userRepository){
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) {
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

    }
