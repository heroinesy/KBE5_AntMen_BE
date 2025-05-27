package com.antmen.antwork.customer.service;

import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.UserRole;
import com.antmen.antwork.common.infra.repository.UserRepository;
import com.antmen.antwork.customer.api.request.CustomerSignupRequest;
import com.antmen.antwork.customer.api.response.CustomerProfileResponse;
import com.antmen.antwork.customer.domain.entity.CustomerDetail;
import com.antmen.antwork.customer.infra.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

//    private final Path uploadDir = Paths.get("/uploads"); // 저장할 경로 /uploads

    @Transactional
    public void signUp(CustomerSignupRequest request) {

        if (userRepository.findByUserLoginId(request.getUserLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다."); // custom exception으로 변경
        }

//        String imagePath = null;
//
//        MultipartFile image = request.getUserProfile();
//
//        if (image != null && !image.isEmpty()) {
//            try {
//
//                if (!Files.exists(uploadDir)) {
//                    Files.createDirectories(uploadDir);
//                }
//
//                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//
//                Path path = uploadDir.resolve(fileName);
//
//                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//                imagePath = "/uploads/" + fileName; // DB저장용 상대 경로 ( 정적 리소스 매핑 필요)
//
//            } catch (IOException e) {
//                throw new RuntimeException("프로필 이미지 업로드 실패",e);
//            }
//        }

        // mapper 생성해야함
        User user = User.builder()
                .userLoginId(request.getUserLoginId())
                .userPassword(request.getUserPassword()) // 암호화 필요
                .userName(request.getUserName())
                .userTel(request.getUserTel())
                .userEmail(request.getUserEmail())
                .userBirth(request.getUserBirth())
                .userGender(request.getUserGender()) // UserGender.
//                .userProfile(imagePath)
                .userProfile(request.getUserProfile())
                .userRole(UserRole.Customer)
                .build();

        userRepository.save(user);

        CustomerDetail customer = CustomerDetail.builder()
                .user(user)
                .customerPoint(0)
                .build();

        customerRepository.save(customer);

    }

    @Transactional
    public CustomerProfileResponse getProfile(Long loginId) {
        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        CustomerDetail customerDetail = customerRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("customer 정보가 존재하지 않습니다."));

        return CustomerProfileResponse.builder()
                .userName(user.getUserName())
                .userTel(user.getUserTel())
                .userEmail(user.getUserEmail())
                .userGender(user.getUserGender())
                .userProfile(user.getUserProfile())
                .userBirth(user.getUserBirth())
                .customerPoint(customerDetail.getCustomerPoint())
                .build();
    }
}
