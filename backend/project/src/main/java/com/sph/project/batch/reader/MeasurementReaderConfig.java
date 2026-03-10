package com.sph.project.batch.reader;

import com.sph.project.batch.dto.MeasurementExcel;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class MeasurementReaderConfig {

    @Bean
    public PoiItemReader<MeasurementExcel> measurementExcelItemReader() throws Exception {
        PoiItemReader<MeasurementExcel> reader = new PoiItemReader<>();
        reader.setResource(new ClassPathResource("data/측정값.xlsx"));
        reader.setLinesToSkip(1);
        reader.setRowMapper(measurementExcelRowMapper());
        reader.afterPropertiesSet();
        return reader;
    }

    private RowMapper<MeasurementExcel> measurementExcelRowMapper() {
        return (RowSet rs) -> {
            String[] row = rs.getCurrentRow();

            return MeasurementExcel.builder()
                    .systemId(parseLong(get(row, 0)))
                    .locationSystemId(parseLong(get(row, 1)))
                    .maxVal(parseDouble(get(row, 2)))
                    .minVal(parseDouble(get(row, 3)))
                    .avgVal(parseDouble(get(row, 4)))
                    .build();
        };
    }

    // 왜 Location은 상관없었나? -> x,y 또는 address 필드만 null이 존재하므로 맨앞 맨뒤에는 null이 없음
    // 엑셀 맨 뒤에 null값이 있어서 직접 인덱스로 접근하면 스킵되서 에러가 남
    // 값들은 Null 허용이므로 null 값을 채워서 저장해야됨
    private String get(String[] row, int index) {
        if (row == null || index >= row.length) {
            return null;
        }
        return row[index];
    }

    // 측정위치와 마찬가지로 측정값도 max_val, min_val, avg_val이 비어있는 값이 있음
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

}
