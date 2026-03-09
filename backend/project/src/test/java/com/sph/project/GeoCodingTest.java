package com.sph.project;

import com.sph.project.common.util.GeoCodingResponse;
import com.sph.project.common.util.GeoCodingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeoCodingTest {

    @Autowired
    private GeoCodingService geoCodingService;

    @Test
    @DisplayName("Geocode Test: 주소 입력 시 정확한 x(경도), y(위도)를 반환한다.")
    void geocodeTest() {
        String address = "서울특별시 강남구 테헤란로";

        GeoCodingResponse result = geoCodingService.geocode(address);

        System.out.println("=== Geocode 결과 ===");
        System.out.println("주소: " + result.getAddress());
        System.out.println("경도(x): " + result.getX() + ", 위도(y): " + result.getY());
    }

    @Test
    @DisplayName("Reverse-Geocode Test: 좌표 입력 시 정확한 주소를 반환한다.")
    void reverseGeocodeTest() {
        double testX = 127.0475882;
        double testY = 37.5041073;

        GeoCodingResponse result = geoCodingService.reverseGeocode(testX, testY);

        System.out.println("=== Reverse Geocode 결과 ===");
        System.out.println("경도(x): " + testX + ", 위도(y): " + testY);
        System.out.println("주소: " + result.getAddress());
    }
}