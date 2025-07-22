package com.antmen.antwork.api.customer;












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
