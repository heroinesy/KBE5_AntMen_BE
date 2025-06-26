package com.antmen.antwork.common.service.serviceAccount;


import com.antmen.antwork.common.api.request.account.CustomerAddressRequest;
import com.antmen.antwork.common.api.request.account.CustomerSignupRequest;
import com.antmen.antwork.common.api.request.account.CustomerUpdateRequest;
import com.antmen.antwork.common.api.response.account.CustomerAddressResponse;
import com.antmen.antwork.common.api.response.account.CustomerProfileResponse;
import com.antmen.antwork.common.api.response.account.CustomerSimpleDto;
import com.antmen.antwork.common.domain.entity.account.CustomerAddress;
import com.antmen.antwork.common.domain.entity.account.CustomerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.domain.exception.UnauthorizedAccessException;
import com.antmen.antwork.common.infra.repository.account.CustomerAddressRepository;
import com.antmen.antwork.common.infra.repository.account.CustomerDetailRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.service.mapper.account.CustomerAddressMapper;
import com.antmen.antwork.common.service.mapper.account.CustomerMapper;
import com.antmen.antwork.common.util.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;
    private final CustomerDetailRepository customerDetailRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerMapper customerMapper;
    private final CustomerAddressMapper customerAddressMapper;
    private final S3UploaderService s3UploaderService;

    @Transactional
    public void signUp(CustomerSignupRequest request) throws IOException {

        if (userRepository.findByUserLoginId(request.getUserLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다."); // custom exception으로 변경
        }

        MultipartFile profileFile = request.getUserProfile();
        String profileUrl = null;
        if (profileFile == null || profileFile.isEmpty()) {
            profileUrl = "https://antmen-bucket.s3.ap-northeast-2.amazonaws.com/customer-profile/default_profile.jpeg";
        } else {
            profileUrl = s3UploaderService.upload(profileFile, "customer-profile");
        }


        User user = customerMapper.toUserEntity(request, profileUrl);
        userRepository.save(user);

        CustomerDetail customer = customerMapper.toCustomerDetailEntity(user);

        customerDetailRepository.save(customer);

    }

    @Transactional(readOnly = true)
    public List<CustomerProfileResponse> getCustomers() {
        List<User> customerList = userRepository.findByUserRole(UserRole.CUSTOMER);

        return customerList.stream()
                .map(user -> {
                    CustomerDetail detail = customerDetailRepository.findByUser(user)
                            .orElseThrow(() -> new IllegalArgumentException("해당 수요자의 상세 정보가 없습니다."));
                    return customerMapper.toDto(user, detail);
                }).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public CustomerProfileResponse getCustomer(Long id) {

        User user = userRepository.findById(id)
                .filter(u -> u.getUserRole() == UserRole.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 수요자입니다."));

        CustomerDetail detail = customerDetailRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("해당 수요자의 상세 정보가 없습니다."));

        return customerMapper.toDto(user, detail);

    }

    @Transactional(readOnly = true)
    public CustomerProfileResponse getProfile(Long loginId) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        CustomerDetail customerDetail = customerDetailRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("customer 정보가 존재하지 않습니다."));

        return customerMapper.toDto(user, customerDetail);
    }

    @Transactional
    public CustomerProfileResponse updateProfile(Long loginId, CustomerUpdateRequest customerUpdateRequest) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        CustomerDetail customerDetail = customerDetailRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("customer 정보가 존재하지 않습니다."));

        user.setUserName(customerUpdateRequest.getUserName());
        user.setUserTel(customerUpdateRequest.getUserTel());
        user.setUserEmail(customerUpdateRequest.getUserEmail());
        user.setUserBirth(customerUpdateRequest.getUserBirth());

        userRepository.save(user);

        return customerMapper.toDto(user, customerDetail);
    }

    @Transactional(readOnly = true)
    public List<CustomerAddressResponse> getAddress(Long loginId) {

        List<CustomerAddressResponse> list = customerAddressRepository.findByUserUserId(loginId).stream()
                .map(customerAddressMapper::toDto)
                .collect(Collectors.toList());

        return list;

    }

    @Transactional
    public void addAddress(Long loginId, CustomerAddressRequest customerAddressRequest) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        CustomerAddress address = customerAddressMapper.toEntity(user, customerAddressRequest);

        customerAddressRepository.save(address);

    }

    @Transactional
    public CustomerAddressResponse updateAddress(
            Long loginId,
            Long addressId,
            CustomerAddressRequest customerAddressRequest) {

        CustomerAddress customerAddress = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 주소가 없습니다."));

        if (!customerAddress.getUser().getUserId().equals(loginId)) {
            throw new UnauthorizedAccessException("해당 주소를 삭제할 권한이 없습니다.");
        }

        customerAddress.setAddressName(customerAddressRequest.getAddressName());
        customerAddress.setAddressAddr(customerAddressRequest.getAddressAddr());
        customerAddress.setAddressDetail(customerAddressRequest.getAddressDetail());
        customerAddress.setAddressArea(customerAddressRequest.getAddressArea());

        return customerAddressMapper.toDto(customerAddressRepository.save(customerAddress));

    }

    @Transactional
    public void deleteAddress(Long loginId, Long addressId) {

        CustomerAddress customerAddress = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 주소가 없습니다."));

        if (!customerAddress.getUser().getUserId().equals(loginId)) {
            throw new UnauthorizedAccessException("해당 주소를 삭제할 권한이 없습니다.");
        }

        customerAddressRepository.deleteById(addressId);
    }

    @Transactional
    public CustomerSimpleDto checkUser(Long userId) {
        return new CustomerSimpleDto(customerDetailRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다.")));
    }

    @Transactional
    public void updateProfileImage(Long loginId, MultipartFile userProfile) throws IOException {
        final String DEFAULT_PROFILE_URL = "https://antmen-bucket.s3.ap-northeast-2.amazonaws.com/customer-profile/default_profile.jpeg";

        User user = userRepository.findById(loginId)
                .filter(u -> u.getUserRole() == UserRole.CUSTOMER)
                .orElseThrow(() -> new NotFoundException("해당 사용자가 없습니다."));

        String newProfileUrl = "";
        if (userProfile == null || userProfile.isEmpty()) {
            newProfileUrl = DEFAULT_PROFILE_URL;
        } else {
            newProfileUrl = s3UploaderService.upload(userProfile, "customer-profile");

            if (!user.getUserProfile().contains(DEFAULT_PROFILE_URL)) {
                s3UploaderService.deleteFile(user.getUserProfile());
            }

        }

        user.setUserProfile(newProfileUrl);

    }
}
