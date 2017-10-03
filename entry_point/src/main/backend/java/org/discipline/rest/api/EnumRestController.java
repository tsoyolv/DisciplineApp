package org.discipline.rest.api;

import com.olts.discipline.entity.ChallengeSphere;
import com.olts.discipline.rest.dto.EnumValuesDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OLTS on 24.09.2017.
 */
@RestController("/api/enum")
public class EnumRestController {

    @GetMapping("/sphere")
    private @ResponseBody
    ResponseEntity<EnumValuesDto> getSphereEnumValues() {
        List<String> values = Arrays.stream(ChallengeSphere.values()).map(Enum::toString).collect(Collectors.toList());
        return ResponseEntity.ok(new EnumValuesDto(values));
    }
}
