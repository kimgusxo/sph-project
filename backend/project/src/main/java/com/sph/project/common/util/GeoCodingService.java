package com.sph.project.common.util;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.sph.project.common.error.CustomException;
import com.sph.project.common.error.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


// x = lng(경도), y = lat(위도)
@Slf4j
@Service
public class GeoCodingService {

    @Value("${google.api.key}")
    private String apiKey;

    private GeoApiContext context;

    @PostConstruct
    public void init() {
        this.context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    public GeoCodingResponse geocode(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        try {
            log.info("입력된 주소: {}", address);
            GeocodingResult[] results = GeocodingApi.geocode(context, address)
                    .language("ko")
                    .await();

            if (results == null || results.length == 0) {
                throw new CustomException(ErrorCode.GEOCODING_NOT_FOUND);
            }

            return convert(results);
        } catch (ApiException | IOException e) {
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ErrorCode.EXTERNAL_API_TIMEOUT);
        }
    }

    // 위도/경도 거꾸로임
    public GeoCodingResponse reverseGeocode(double x, double y) {
        if (x < -180 || x > 180 || y < -90 || y > 90) {
            throw new CustomException(ErrorCode.INVALID_COORDINATE);
        }

        try {
            log.info("경도(x): {}, 위도(y): {}", x, y);
            // 구글 SDK는 LatLng(위도, 경도) 순서임
            GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(y, x))
                    .language("ko")
                    .await();

            if (results == null || results.length == 0) {
                throw new CustomException(ErrorCode.REVERSE_GEOCODING_NOT_FOUND);
            }

            return convert(results);
        } catch (ApiException | IOException e) {
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ErrorCode.EXTERNAL_API_TIMEOUT);
        }
    }

    private GeoCodingResponse convert(GeocodingResult[] results) {
        if (results.length > 0) {
            GeocodingResult res = results[0];
            return GeoCodingResponse.builder()
                    .x(res.geometry.location.lng)
                    .y(res.geometry.location.lat)
                    .address(res.formattedAddress)
                    .build();
        }
        return null;
    }
}