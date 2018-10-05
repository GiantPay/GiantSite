package network.giantpay.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ImageUtils {

    private final static Splitter splitter = Splitter.on("|")
            .trimResults()
            .omitEmptyStrings();

    public static List<String> getImages(String images) {
        if (Strings.isNullOrEmpty(images)) {
            return ImmutableList.of();
        }
        return splitter.splitToList(images);
    }

    public static String getImages(MultiValueMap params) {
        Object images = params.get("images");
        if (images != null && images instanceof Collection) {
            return ((Collection<String>) images).stream()
                    .collect(Collectors.joining("|"));
        }
        return null;
    }
}
