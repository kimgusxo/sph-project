package com.sph.project.batch.reader;

import com.sph.project.batch.dto.LocationExcel;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class LocationReaderConfig {

    @Bean
    public PoiItemReader<LocationExcel> locationExcelItemReader() throws Exception {
        PoiItemReader<LocationExcel> reader = new PoiItemReader<>();
        reader.setResource(new ClassPathResource("data/측정위치.xlsx"));
        reader.setLinesToSkip(1);
        reader.setRowMapper(locationExcelRowMapper());
        reader.afterPropertiesSet();
        return reader;
    }

    private RowMapper<LocationExcel> locationExcelRowMapper() {
        return (RowSet rs) -> {
            String[] row = rs.getCurrentRow();
            for(String r : row) {
                System.out.println(r);
            }
            return LocationExcel.builder()
                    .systemId(parseLong(row[0]))
                    .x(parseDouble(row[1]))
                    .y(parseDouble(row[2]))
                    .address(parseString(row[3]))
                    .lightSourceCount(parseInteger(row[4]))
                    .build();
        };
    }

    // x, y 또는 주소가 비어있어서 파싱에 문제가 생김 따라서 null을 해결하는 헬퍼메소드가 필요해서 만듬
    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.parseLong(value);
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Double.parseDouble(value);
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    private String parseString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }
}
