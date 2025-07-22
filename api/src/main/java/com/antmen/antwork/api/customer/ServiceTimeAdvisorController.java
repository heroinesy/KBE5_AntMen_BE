package com.antmen.antwork.api.customer;

import com.antmen.antwork.core.exception.NotFoundException;
import com.antmen.antwork.domain.reservation.rule.ServiceTimeAdvisor;
import com.antmen.antwork.domain.user.entity.CustomerAddress;
import com.antmen.antwork.domain.user.repository.CustomerAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recommend-duration")
public class ServiceTimeAdvisorController {
    private final ServiceTimeAdvisor serviceTimeAdvisor;
    private final CustomerAddressRepository customerAddressRepository;

    @GetMapping
    public ResponseEntity<Short> getRecommendDuration(@RequestParam("address_id") Long addressId) {
        CustomerAddress customerAddress = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("주소를 찾을 수 없습니다."));
        int area = customerAddress.getAddressArea();
        short recommended = serviceTimeAdvisor.recommedTime(area);
        return ResponseEntity.ok(recommended);
    }
}
