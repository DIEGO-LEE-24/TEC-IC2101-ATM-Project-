package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime unmarshal(String xml) throws Exception {
        return (xml == null || xml.isEmpty())
            ? null
            : LocalDateTime.parse(xml, FMT);
    }

    @Override
    public String marshal(LocalDateTime obj) throws Exception {
        return (obj == null)
            ? ""
            : obj.format(FMT);
    }
}
