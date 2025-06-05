package com.antmen.antwork.common.service.serviceAccount;

import com.antmen.antwork.common.api.request.account.CustomerAddressRequest;
import com.antmen.antwork.common.api.request.account.CustomerSignupRequest;
import com.antmen.antwork.common.api.request.account.CustomerUpdateRequest;
import com.antmen.antwork.common.api.response.account.CustomerAddressResponse;
import com.antmen.antwork.common.api.response.account.CustomerProfileResponse;
import com.antmen.antwork.common.domain.entity.account.CustomerAddress;
import com.antmen.antwork.common.domain.entity.account.CustomerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.exception.UnauthorizedAccessException;
import com.antmen.antwork.common.infra.repository.account.CustomerAddressRepository;
import com.antmen.antwork.common.infra.repository.account.CustomerRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.service.mapper.account.CustomerAddressMapper;
import com.antmen.antwork.common.service.mapper.account.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerMapper customerMapper;
    private final CustomerAddressMapper customerAddressMapper;

    @Transactional
    public void signUp(CustomerSignupRequest request) {

        if (userRepository.findByUserLoginId(request.getUserLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다."); // custom exception으로 변경
        }

        // 프로필 업로드 시 추가 필요

        User user = customerMapper.toUserEntity(request);

        userRepository.save(user);

        CustomerDetail customer = customerMapper.toCustomerDetailEntity(user);

        customerRepository.save(customer);

    }

    @Transactional(readOnly = true)
    public CustomerProfileResponse getProfile(Long loginId) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        CustomerDetail customerDetail = customerRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("customer 정보가 존재하지 않습니다."));

        return customerMapper.toDto(user, customerDetail);
    }

    @Transactional
    public CustomerProfileResponse updateProfile(Long loginId, CustomerUpdateRequest customerUpdateRequest) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        CustomerDetail customerDetail = customerRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("customer 정보가 존재하지 않습니다."));

        user.setUserName(customerUpdateRequest.getUserName());
        user.setUserTel(customerUpdateRequest.getUserTel());
        user.setUserEmail(customerUpdateRequest.getUserEmail());
        user.setUserGender(customerUpdateRequest.getUserGender());
        user.setUserBirth(customerUpdateRequest.getUserBirth());
        user.setUserProfile(customerUpdateRequest.getUserProfile());

        userRepository.save(user);

        return customerMapper.toDto(user, customerDetail);
    }

    /**
     * 면적 유효성 검사
     */
    private static final int MAX_ALLOWED_AREA = 99; // 최대 면적
    private static final int MIN_ALLOWED_AREA = 1; // 최소 면적

    private void validateArea(Integer area) {
        if (area == null || area < MIN_ALLOWED_AREA || area > MAX_ALLOWED_AREA){
            throw new IllegalArgumentException("1평 이상 99평 이하 면적에서만 예약이 가능합니다.");
        }
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

        validateArea(customerAddressRequest.getAddressArea());

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

        validateArea(customerAddressRequest.getAddressArea());

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
}
