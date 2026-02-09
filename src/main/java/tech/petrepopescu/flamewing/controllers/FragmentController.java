package tech.petrepopescu.flamewing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.petrepopescu.flamewing.format.FlamewingResponse;
import tech.petrepopescu.flamewing.format.Format;
import tech.petrepopescu.flamewing.format.Result;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;
import tech.petrepopescu.flamewing.spring.config.FlamewingConfiguration;
import tech.petrepopescu.flamewing.views.View;

import java.util.List;

@Controller
public class FragmentController {
    private final FlamewingConfiguration configuration;
    private final FlamewingSpecialElementsUtil specialElementsUtil;

    public FragmentController(FlamewingConfiguration configuration, FlamewingSpecialElementsUtil specialElementsUtil) {
        this.configuration = configuration;
        this.specialElementsUtil = specialElementsUtil;
    }

    @GetMapping("/flamewing/fragment")
    public Result getFragment(@RequestParam(name = "name", required = false, defaultValue = "") String fragmentName,
                              @RequestParam(value = "param", required = false)List<String> params) {
        if (params == null) {
            params = List.of();
        }

        if (!configuration.isFragmentRetrieveEnabled()) {
            return FlamewingResponse.notFound();
        }

        Format format = View.ofNullableWithStringArgs(fragmentName, params);
        if (format == null) {
            return FlamewingResponse.notFound();
        }

        return FlamewingResponse.ok(format);
    }
}
